package com.company.currency.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.company.currency.client.domain.CountryInfo;

@FeignClient(value = "countryInfoClient", url = "${info.infocountry}")
public interface CountryInfoClient {
	
	@GetMapping(value = { "/alpha/{iso}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CountryInfo> getCountryInfo(@PathVariable(value="iso") String iso)  throws Exception;
}
