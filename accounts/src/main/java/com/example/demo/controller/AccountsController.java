package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Accounts;
import com.example.demo.model.Customer;
import com.example.demo.repository.AccountsRepository;

@RestController
public class AccountsController {

	@Autowired
	private AccountsRepository accountsRepository;
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@PostMapping("/myAccount")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if(accounts != null) {
			return accounts;
		} else {
			return null;
		}
	}
}
