package com.company.currency.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.company.currency.client.domain.CountryIp;

@FeignClient(value = "countryIpClient", url = "${info.countryip}")
public interface CountryIpClient {
	@GetMapping(value = { "/ip?{ip}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CountryIp> getCountryIp(@PathVariable(value="ip") String ip) throws Exception;

}
