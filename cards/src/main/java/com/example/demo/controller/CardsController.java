package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Cards;
import com.example.demo.model.Customer;
import com.example.demo.repository.CardsRepository;

@RestController
public class CardsController {

	@Autowired
	private CardsRepository cardsRepository;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@PostMapping("/myCards")
	public List<Cards> getCardDetails(@RequestBody Customer customer) {
		List<Cards> cards = cardsRepository.findByCustomerId(customer.getCustomerId());
		if (cards != null) {
			return cards;
		} else {
			return null;
		}
	}
}
