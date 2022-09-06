package com.pfi.crm.multitenant.tenant.repository.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.test.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
	Optional<Customer> findByPersonFirstName(String firstName);
	Optional<Customer> findByPersonLastName(String lastName);
	
    Boolean existsByPersonFirstName(String firstName);
    Boolean existsByPersonLastName(String lastName);
    
    
}