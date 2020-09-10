package com.company.currency.service;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.company.currency.AbstractTest;
import com.company.currency.client.CountryInfoClient;
import com.company.currency.client.CountryIpClient;
import com.company.currency.client.UsdCotizationClient;
import com.company.currency.client.domain.CountryInfo;
import com.company.currency.client.domain.CountryIp;
import com.company.currency.client.domain.UsdQuotation;
import com.company.currency.entity.BlackListIp;
import com.company.currency.model.IpCountryInfo;
import com.company.currency.repository.BlackListIpRepository;
import com.company.currency.service.impl.IpCountryInfoServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class IpCountryInfoServiceImplTest extends AbstractTest {
	private static final String IP = "0.0.0.0";

	private static final String COUNTRYIPFILE = "/json/ipCountryInfo.json";
	private static final String INFOCOUNTRYFILE = "/json/infoCountry.json";
	private static final String COUNTRYCURRENCYFILE = "/json/countryCurrency.json";
	private CountryIp countryIp;
	private CountryInfo countryInfo;
	private UsdQuotation usdQuotation;
	private BlackListIp blackListIp4Ip;
	@Mock
	private CountryInfoClient countryInfoClient;
	@Mock
	private CountryIpClient countryIpClient;
	@Mock
	private UsdCotizationClient usdCotizationClient;
	@Mock
	private BlackListIpRepository blackListIpRepository;
	@InjectMocks
	private IpCountryInfoServiceImp ipCountryInfoServiceImpl;

	@BeforeEach
	public void init() throws IOException {
		blackListIp4Ip = new BlackListIp();
		blackListIp4Ip.setId("1");
		blackListIp4Ip.setIp(IP);
		countryIp = (CountryIp) getObject(COUNTRYIPFILE, CountryIp.class);
		countryInfo = (CountryInfo) getObject(INFOCOUNTRYFILE, CountryInfo.class);
		usdQuotation = (UsdQuotation) getObject(COUNTRYCURRENCYFILE, UsdQuotation.class);
		ReflectionTestUtils.setField(ipCountryInfoServiceImpl, "access_key", "test");
	}

	@Test
	public void shouldGetIpCountryInfo() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryInfoClient.getCountryInfo(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryInfo>(countryInfo, HttpStatus.OK));
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(countryIp, HttpStatus.OK));
		when(usdCotizationClient.getUsdQuotation(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ResponseEntity<UsdQuotation>(usdQuotation, HttpStatus.OK));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals("Germany", response.getBody().getCountryName());
		Assertions.assertEquals("EUR", response.getBody().getCurrency().get(0).getCurrency());
		Assertions.assertEquals("1", response.getBody().getCurrency().get(0).getEurValue());
		Assertions.assertEquals("DE", response.getBody().getIsoCode());
		Assertions.assertEquals("0.0.0.0", response.getBody().getIp());

	}

	@Test
	public void shouldDontGetIpCountryInfoWhenIsInBlackList() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.of(blackListIp4Ip));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
	@Test
	public void shouldDontGetIpCountryWhenIsInBlackListThrowsException() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenThrow(new RuntimeException());
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void shouldGetPartialContentWhenTheCountryClientFails() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(HttpStatus.BAD_GATEWAY));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
	}

	@Test
	public void shouldGetPartialContentWhenTheCountryClientThrowsException() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryIpClient.getCountryIp(Mockito.anyString())).thenThrow(new RuntimeException());
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void shouldGetPartialContentWhenTheInfoClientFails() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryInfoClient.getCountryInfo(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryInfo>(HttpStatus.BAD_REQUEST));
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(countryIp, HttpStatus.OK));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
	}

	@Test
	public void shouldGetPartialContentWhenTheInfoClientThrowsException() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryInfoClient.getCountryInfo(Mockito.anyString())).thenThrow(new RuntimeException());
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(countryIp, HttpStatus.OK));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void shouldGetPartialContentWhenTheQuotationClientFails() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryInfoClient.getCountryInfo(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryInfo>(countryInfo, HttpStatus.OK));
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(countryIp, HttpStatus.OK));
		when(usdCotizationClient.getUsdQuotation(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ResponseEntity<UsdQuotation>(HttpStatus.INTERNAL_SERVER_ERROR));
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void shouldGetPartialContentWhenTheQuotationClientThrowsException() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		when(countryInfoClient.getCountryInfo(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryInfo>(countryInfo, HttpStatus.OK));
		when(countryIpClient.getCountryIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<CountryIp>(countryIp, HttpStatus.OK));
		when(usdCotizationClient.getUsdQuotation(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new RuntimeException());
		ResponseEntity<IpCountryInfo> response = ipCountryInfoServiceImpl.getIpInfoCountry(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
