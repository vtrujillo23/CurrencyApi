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
import com.company.currency.controller.IpController;
import com.company.currency.entity.BlackListIp;
import com.company.currency.model.IpCountryInfo;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("int")
public class IpControllerIntegrationTest extends AbstractIntTest{
	private static final String IP="127.0.0.1";
	private static final String IPBLACKLIST="127.0.0.2";
	@Autowired
	private BlackListController blackListController;
	
	@Autowired
	private IpController ipController;
	
	@BeforeEach
	public void init() throws IOException {
		startMongoEmbbed();
		startWireMock();
	}
	
	@AfterEach
	public void shutdown() throws IOException {
		stopWireMock();
	}
	
	@Test
	public void shouldGetIpInfo() {
		ResponseEntity<IpCountryInfo> response=ipController.getIpCountryInfo(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertNotNull(response.getBody().getCountryName());
		Assertions.assertNotNull(response.getBody().getCurrency());
		Assertions.assertNotNull(response.getBody().getIp());
		Assertions.assertNotNull(response.getBody().getCurrency());
		Assertions.assertFalse(response.getBody().getCurrency().isEmpty());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void shouldDontGetIpInfoWhenItIsInBlackList() {
		BlackListIp blackListIp=new BlackListIp();
		blackListIp.setIp(IPBLACKLIST);
		blackListController.saveIp(blackListIp);
		ResponseEntity<IpCountryInfo> response=ipController.getIpCountryInfo(IPBLACKLIST);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}
	
}
