package com.company.currency.client.domain;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsdQuotation {
	private Map<String,Double> rates;
}
