package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Loans;

public interface LoansRepository extends CrudRepository<Loans, Long> {

	List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);
}
