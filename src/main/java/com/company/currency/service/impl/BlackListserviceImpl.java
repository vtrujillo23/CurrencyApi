package com.company.currency.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.currency.entity.BlackListIp;
import com.company.currency.repository.BlackListIpRepository;
import com.company.currency.service.BlackListService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BlackListserviceImpl implements BlackListService {
	@Autowired
	private BlackListIpRepository blackListIpRepository;

	@Override
	public ResponseEntity<BlackListIp> saveBlackListIp(BlackListIp blackListIp) {
		try {
			blackListIp.setId(UUID.randomUUID().toString());
			blackListIpRepository.save(blackListIp);
			return new ResponseEntity<>(blackListIp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<BlackListIp> getBlackListIpByIp(String ip) {
		try {
			Optional<BlackListIp> optionalBlackList = blackListIpRepository.findByIp(ip);
			return optionalBlackList.isPresent() ? new ResponseEntity<>(optionalBlackList.get(), HttpStatus.OK)
					: (new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
		} catch (Exception e) {
			log.error("Error when the ip saved", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
