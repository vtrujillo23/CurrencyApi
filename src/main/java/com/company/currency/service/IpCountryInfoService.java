package com.company.currency.service;

import org.springframework.http.ResponseEntity;

import com.company.currency.model.IpCountryInfo;

public interface IpCountryInfoService {
	public ResponseEntity<IpCountryInfo> getIpInfoCountry(String ip);
}
