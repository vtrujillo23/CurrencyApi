package com.company.currency.entity;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlackListIp {
	@Id
	private String id;
	@NotNull
	private String ip;
}
