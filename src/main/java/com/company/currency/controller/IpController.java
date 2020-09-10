package com.company.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.company.currency.model.IpCountryInfo;
import com.company.currency.service.impl.IpCountryInfoServiceImp;

@RestController
public class IpController {
	@Autowired
	private IpCountryInfoServiceImp ipCountryInfoServiceImp;
	
	@GetMapping(value = "/ip/{ip}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IpCountryInfo> getIpCountryInfo(@PathVariable String ip){
		return ipCountryInfoServiceImp.getIpInfoCountry(ip);
	}

}
