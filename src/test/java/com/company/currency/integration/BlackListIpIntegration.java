package com.company.currency.integration;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.company.currency.controller.BlackListController;
import com.company.currency.entity.BlackListIp;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("int")
public class BlackListIpIntegration extends AbstractIntTest{
	private static final String IP="127.0.0.1";
	
	@BeforeEach
	public void init() throws IOException {
		startMongoEmbbed();
		startWireMock();
	}
	
	@AfterEach
	public void shutdown() throws IOException {
		stopWireMock();
	}
	
	@Autowired
	private BlackListController blackListController;
	
	@Test
	public void shouldSaveTheIpAndGetTheIP() {
		BlackListIp blackListIp=new BlackListIp();
		blackListIp.setIp(IP);
		ResponseEntity<BlackListIp> response=blackListController.saveIp(blackListIp);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertNotNull(response.getBody().getId());
		Assertions.assertNotNull(response.getBody().getIp());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		ResponseEntity<BlackListIp> responseget=blackListController.getIp(IP);
		Assertions.assertNotNull(responseget);
		Assertions.assertNotNull(responseget.getBody());
		Assertions.assertNotNull(responseget.getBody().getId());
		Assertions.assertNotNull(responseget.getBody().getIp());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertEquals(IP, response.getBody().getIp());
	}
	

}
