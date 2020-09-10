package com.company.currency.service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import com.company.currency.repository.BlackListIpRepository;
import com.company.currency.service.impl.BlackListserviceImpl;

@ExtendWith(MockitoExtension.class)
public class BlackListServiceTest extends AbstractTest {
	private static final String IP = "0.0.0.0";

	@Mock
	private BlackListIpRepository blackListIpRepository;

	@InjectMocks
	private BlackListserviceImpl blackListService;

	private BlackListIp blackListIp;

	@BeforeEach
	public void init() {
		blackListIp = new BlackListIp();
		blackListIp.setId("1");
		blackListIp.setIp(IP);
	}

	@Test
	public void shouldGetIp() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.of(blackListIp));
		ResponseEntity<BlackListIp> response = blackListService.getBlackListIpByIp(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(blackListIp.getId(), response.getBody().getId());
		Assertions.assertEquals(blackListIp.getIp(), response.getBody().getIp());
	}

	@Test
	public void shouldGetResponseWhenError() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenThrow(new Exception());
		ResponseEntity<BlackListIp> response = blackListService.getBlackListIpByIp(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void shouldGetResponseWhenNotFound() throws Exception {
		when(blackListIpRepository.findByIp(Mockito.anyString())).thenReturn(Optional.empty());
		ResponseEntity<BlackListIp> response = blackListService.getBlackListIpByIp(IP);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void shouldSaveIp() {
		BlackListIp blackListIp = new BlackListIp();
		blackListIp.setIp(IP);
		ResponseEntity<BlackListIp> response=blackListService.saveBlackListIp(blackListIp);
		verify(blackListIpRepository, atLeast(1)).save(Mockito.any(BlackListIp.class));
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertNotNull(response.getBody().getId());
		Assertions.assertNotNull(response.getBody().getIp());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void shouldThrowExceptionWhenSaveIp() {
		when(blackListIpRepository.save(Mockito.any(BlackListIp.class))).thenThrow(new RuntimeException());
		BlackListIp blackListIp = new BlackListIp();
		blackListIp.setIp(IP);
		ResponseEntity<BlackListIp> response=blackListService.saveBlackListIp(blackListIp);
		Assertions.assertNotNull(response);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

}
