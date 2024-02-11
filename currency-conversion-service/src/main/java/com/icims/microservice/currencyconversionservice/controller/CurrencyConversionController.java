package com.icims.microservice.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.icims.microservice.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.icims.microservice.currencyconversionservice.bean.CurrencyConversionBean;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/*@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {
    
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}*/
@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean currencyConverter(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		// Feign- problem 1
		Map<String, String> urivariables = new HashMap<>();
		urivariables.put("from", from);
		urivariables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
				urivariables);
		CurrencyConversionBean response = responseEntity.getBody();

		if (response != null) {
			return new CurrencyConversionBean(response.getId(), from, to, quantity,
					quantity.multiply(response.getConversionRate()), response.getPort(), response.getConversionRate());

		}
		return null;
	}

	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	@CircuitBreaker(name = "exchangeService", fallbackMethod = "getDefaultExchangeRate")
	public CurrencyConversionBean currencyConverterFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);

		if (response != null) {
			return new CurrencyConversionBean(response.getId(), from, to, quantity,
					quantity.multiply(response.getConversionRate()), response.getPort(), response.getConversionRate());

		}
		return null;
	}

	public CurrencyConversionBean getDefaultExchangeRate(Exception e) {
		return new CurrencyConversionBean();

	}

}
