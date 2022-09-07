package com.example.demo.context;

import org.springframework.stereotype.Component;

@Component
public class RequestContext {

	public static final String CORRELATION_ID = "bank-correlation=id";
	
	private String correlationId = new String();

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
	
	
}
