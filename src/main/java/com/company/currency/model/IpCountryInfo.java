package com.company.currency.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpCountryInfo {
	
	private String ip;
	private String isoCode;
	private String countryName;
	private List<CurrencyInfo> currency;
	

}
