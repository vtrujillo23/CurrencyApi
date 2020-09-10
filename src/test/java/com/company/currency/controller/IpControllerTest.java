package com.company.currency.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;

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
import com.company.currency.model.IpCountryInfo;
import com.company.currency.service.impl.IpCountryInfoServiceImp;

@ExtendWith(MockitoExtension.class)
public class IpControllerTest extends AbstractTest {
	
	private static final String IP = "0.0.0.0";
	private static final String RESPONSE_JSON_FILE="/json/finalResponse.json";
	private IpCountryInfo ipCountryInfo;
	@Mock
	private IpCountryInfoServiceImp ipCountryInfoServiceImp;
	@InjectMocks
	private IpController ipController;

	@BeforeEach
	public void init() throws IOException {
		ipCountryInfo=(IpCountryInfo) getObject(RESPONSE_JSON_FILE,IpCountryInfo.class);
	}
	
	@Test
	public void shouldGetIpCountryInfo() {
		when(ipCountryInfoServiceImp.getIpInfoCountry(Mockito.anyString())).thenReturn(new ResponseEntity<>(ipCountryInfo,HttpStatus.OK));
		ResponseEntity<IpCountryInfo> response=ipController.getIpCountryInfo(IP);
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assertions.assertNotNull(response.getBody());
	}

}
