package com.icims.microservice.currencyexchangeservice;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.icims.microservice.currencyexchangeservice.bean.ExchangeValue;
import com.icims.microservice.currencyexchangeservice.repository.ExchangeValueRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class CurrencyExchangeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeServiceApplication.class, args);
	}

    @Bean
    CommandLineRunner insertExchangeValue(ExchangeValueRepository repository) {
		return args ->{ 
				repository.save(new ExchangeValue("USD","IND",BigDecimal.valueOf(75)));
				repository.save(new ExchangeValue("FRA","INR",BigDecimal.valueOf(70)));
				repository.save(new ExchangeValue("BRA","IND",BigDecimal.valueOf(65)));
				repository.save(new ExchangeValue("CHZ","INR",BigDecimal.valueOf(60)));
				};
	}

}
