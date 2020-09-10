package com.company.currency.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.currency.client.domain.UsdQuotation;

@FeignClient(value = "UsdCotizationClient", url = "${info.infocurrency}")
public interface UsdCotizationClient {
	
	@GetMapping(value = { "/latest" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsdQuotation> getUsdQuotation(@RequestParam("access_key") String access_key,@RequestParam("symbols") String symbols);
}
