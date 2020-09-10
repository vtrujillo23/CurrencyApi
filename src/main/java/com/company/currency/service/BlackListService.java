package com.company.currency.service;

import org.springframework.http.ResponseEntity;

import com.company.currency.entity.BlackListIp;


public interface BlackListService {
	
	public ResponseEntity<BlackListIp>  saveBlackListIp(final BlackListIp blackListIp);
	public ResponseEntity<BlackListIp> getBlackListIpByIp(final String ip);
}
