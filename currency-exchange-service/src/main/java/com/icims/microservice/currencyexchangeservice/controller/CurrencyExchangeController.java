package com.icims.microservice.currencyexchangeservice.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.icims.microservice.currencyexchangeservice.bean.ExchangeValue;
import com.icims.microservice.currencyexchangeservice.repository.ExchangeValueRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class CurrencyExchangeController {
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private ExchangeValueRepository exchangeValueRepository;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	
	public ExchangeValue retrieveExchangeValue(@PathVariable String from,@PathVariable String to) {
		
		logger.info("retrieveExchangeValue called with {} to {}", from, to);
		ExchangeValue exchangeValue = exchangeValueRepository.findByFromAndTo(from, to);
		
		if(exchangeValue ==null) {
			throw new RuntimeException("Unable to Find data for " + from + " to " + to);
		}
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		return exchangeValue;
	}

}
