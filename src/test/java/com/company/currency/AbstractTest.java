package com.company.currency;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractTest {
	protected Object getObject(final String filepath, final Class type) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new ClassPathResource(filepath).getInputStream(), type);
	}
}
