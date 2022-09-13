package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.CardsServiceConfig;
import com.example.demo.model.Cards;
import com.example.demo.model.Customer;
import com.example.demo.model.Properties;
import com.example.demo.repository.CardsRepository;

@RestController
public class CardsController {

	private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

	@Autowired
	private CardsRepository cardsRepository;

	@Autowired
	CardsServiceConfig cardsConfig;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@PostMapping("/myCards")
	public List<Cards> getCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
			@RequestBody Customer customer) {
		logger.info("getCardDetails() method called");
		System.out.println("Invoking Cards Microservice");
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		if (cards != null) {
			return cards;
		} else {
			return null;
		}
	}

	@GetMapping("/cards/properties")
	public Properties getPropertiesDetails() {
		return new Properties(cardsConfig.getMsg(), cardsConfig.getBuildVersion(), cardsConfig.getMailDetails(),
				cardsConfig.getActiveBranches());
	}
}
