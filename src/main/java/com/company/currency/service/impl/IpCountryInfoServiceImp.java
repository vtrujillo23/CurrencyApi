package com.company.currency.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.currency.client.CountryInfoClient;
import com.company.currency.client.CountryIpClient;
import com.company.currency.client.UsdCotizationClient;
import com.company.currency.client.domain.CountryInfo;
import com.company.currency.client.domain.CountryIp;
import com.company.currency.client.domain.Currency;
import com.company.currency.client.domain.UsdQuotation;
import com.company.currency.exception.CurrencyException;
import com.company.currency.model.CurrencyInfo;
import com.company.currency.model.IpCountryInfo;
import com.company.currency.repository.BlackListIpRepository;
import com.company.currency.service.IpCountryInfoService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class IpCountryInfoServiceImp implements IpCountryInfoService {
	@Value("${info.access_key}")
	private String access_key;

	@Autowired
	private BlackListIpRepository blackListIpRepository;
	@Autowired
	private CountryIpClient countryIpClient;
	@Autowired
	private CountryInfoClient countryInfoClient;
	@Autowired
	private UsdCotizationClient usdCotizationClient;

	private DecimalFormat df = new DecimalFormat("###,###.#####");

	@Override
	public ResponseEntity<IpCountryInfo> getIpInfoCountry(String ip) {
		try {
			if (isABlackListIp(ip)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			} else {
				IpCountryInfo ipCountryInfo = new IpCountryInfo();
				ipCountryInfo.setIp(ip);
				getCountryIp(ipCountryInfo);
				getCountryInfo(ipCountryInfo);
				getUsdQuotationInfo(ipCountryInfo);
				return getResponseEntity(ipCountryInfo);
			}
		} catch (CurrencyException e) {
			log.error(e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void getCountryIp(IpCountryInfo responseBody) throws CurrencyException {
		try {
			ResponseEntity<CountryIp> responseCountry = countryIpClient.getCountryIp(responseBody.getIp());
			if (responseCountry.getStatusCode().equals(HttpStatus.OK)) {
				responseBody.setIsoCode(responseCountry.getBody().getCountryCode());
				responseBody.setCountryName(responseCountry.getBody().getCountryName());
			}
		} catch (Exception e) {
			throw new CurrencyException(e);
		}
	}

	private void getCountryInfo(IpCountryInfo responseBody) throws CurrencyException {
		try {
			if (responseBody.getIsoCode() != null && !responseBody.getIsoCode().isBlank()) {
				responseBody.setCurrency(new ArrayList<>());
				ResponseEntity<CountryInfo> responseCountry = countryInfoClient
						.getCountryInfo(responseBody.getIsoCode());
				if (responseCountry.getStatusCode().equals(HttpStatus.OK)) {
					responseCountry.getBody().getCurrencies().parallelStream().forEach(currency -> {
						responseBody.getCurrency().add(new CurrencyInfo(currency.getCode(), null));
					});
				}
			}
		} catch (Exception e) {
			throw new CurrencyException(e);
		}
	}

	private void getUsdQuotationInfo(IpCountryInfo responseBody) throws CurrencyException {
		try {
			if (responseBody.getCurrency() != null) {
				responseBody.getCurrency().parallelStream().forEach(currency -> {
					ResponseEntity<UsdQuotation> usdQuotation = usdCotizationClient.getUsdQuotation(access_key,
							currency.getCurrency());
					if (usdQuotation.getBody() != null && usdQuotation.getBody().getRates() != null) {
						currency.setEurValue(
								df.format(1 / usdQuotation.getBody().getRates().get(currency.getCurrency())));
					}
				});
			}
		} catch (Exception e) {
			throw new CurrencyException(e);
		}
	}

	private boolean isABlackListIp(String ip) throws CurrencyException {
		try {
			return blackListIpRepository.findByIp(ip).isPresent();
		} catch (Exception e) {
			throw new CurrencyException(e);
		}
	}

	private ResponseEntity<IpCountryInfo> getResponseEntity(IpCountryInfo ipCountryInfo) {
		if (validResponse(ipCountryInfo)) {
			return new ResponseEntity<IpCountryInfo>(ipCountryInfo, HttpStatus.OK);
		} else {
			return new ResponseEntity<IpCountryInfo>(ipCountryInfo, HttpStatus.PARTIAL_CONTENT);
		}
	}

	private boolean validResponse(IpCountryInfo ipCountryInfo) {
		if (ipCountryInfo.getCountryName() != null && ipCountryInfo.getCurrency() != null
				&& !ipCountryInfo.getCurrency().isEmpty() && ipCountryInfo.getIp() != null
				&& ipCountryInfo.getIsoCode() != null) {
			if (validateCurrency(ipCountryInfo.getCurrency())) {
				return true;
			}
		}
		return false;
	}

	private boolean validateCurrency(List<CurrencyInfo> currencies) {
		for (CurrencyInfo currency : currencies) {
			if (currency.getEurValue() == null) {
				return false;
			}
		}
		return true;
	}

}
