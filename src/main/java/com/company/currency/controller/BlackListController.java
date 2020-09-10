package com.company.currency.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.currency.entity.BlackListIp;
import com.company.currency.service.BlackListService;

@RestController
public class BlackListController {
	@Autowired
	private BlackListService blackListService;
	
	@PostMapping(value = "/blacklist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BlackListIp> saveIp(@Valid @RequestBody BlackListIp blackListIp) {
		return blackListService.saveBlackListIp(blackListIp);
	}
	
	@GetMapping(value = "/blacklist/{ip}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BlackListIp> getIp(@PathVariable String ip) {
		return blackListService.getBlackListIpByIp(ip);
	}
}
