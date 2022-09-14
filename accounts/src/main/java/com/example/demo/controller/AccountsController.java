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

import com.example.demo.config.AccountsServiceConfig;
import com.example.demo.model.Accounts;
import com.example.demo.model.Cards;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerDetails;
import com.example.demo.model.Loans;
import com.example.demo.model.Properties;
import com.example.demo.repository.AccountsRepository;
import com.example.demo.service.client.CardsFeignClient;
import com.example.demo.service.client.LoansFeignClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;

@RestController
public class AccountsController {

	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	AccountsServiceConfig accountsConfig;

	@Autowired
	CardsFeignClient cardsFeignClient;

	@Autowired
	LoansFeignClient loansFeignClient;

	@GetMapping("/hello")
	@RateLimiter(name = "hello", fallbackMethod = "helloFallback")
	public String hello() {
		return "hello";
	}

	private String helloFallback(Throwable t) {
		return "fallback hello";
	}

	@PostMapping("/myAccount")
	@Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
	public Accounts getAccountDetails(@RequestBody Customer customer) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if (accounts != null) {
			return accounts;
		} else {
			return null;
		}
	}

	@GetMapping("/account/properties")
	public Properties getPropertyDetails() {
		return new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());

	}

	@PostMapping("/myCustomerDetails")
	@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationId,
			@RequestBody Customer customer) {
		logger.info("myCustomerDetails() method called");
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationId, customer);
		List<Cards> cards = cardsFeignClient.getCardDetails(correlationId, customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		return customerDetails;
	}

	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("eazybank-correlation-id") String correlationId,
			Customer customer, Throwable t) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoanDetails(correlationId, customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		return customerDetails;
	}
}
