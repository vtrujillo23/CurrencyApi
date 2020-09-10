package com.company.currency.controller;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.company.currency.AbstractTest;
import com.company.currency.entity.BlackListIp;
import com.company.currency.service.BlackListService;

@ExtendWith(MockitoExtension.class)
public class BlackListControllerTest extends AbstractTest {
	private static final String IP = "0.0.0.0";
	private BlackListIp blackListIpResult;
	@Mock
	private BlackListService blackListService;
	@InjectMocks
	private BlackListController blackListController;

	@BeforeEach
	public void init() {
		blackListIpResult = new BlackListIp();
		blackListIpResult.setId(UUID.randomUUID().toString());
		blackListIpResult.setIp(IP);
	}

	@Test
	public void shouldSaveIp() {
		when(blackListService.saveBlackListIp(Mockito.any(BlackListIp.class)))
				.thenReturn(new ResponseEntity<>(blackListIpResult, HttpStatus.OK));
		BlackListIp blackListIp = new BlackListIp();
		blackListIp.setIp(IP);
		ResponseEntity<BlackListIp> response = blackListController.saveIp(blackListIp);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertNotNull(response.getBody().getId());
		Assertions.assertEquals(IP, response.getBody().getIp());
	}

	@Test
	public void shouldGetIp() {
		when(blackListService.getBlackListIpByIp(Mockito.anyString()))
				.thenReturn(new ResponseEntity<>(blackListIpResult, HttpStatus.OK));
		ResponseEntity<BlackListIp> response = blackListController.getIp(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertNotNull(response.getBody().getId());
		Assertions.assertEquals(IP, response.getBody().getIp());
	}
}
