package com.pfi.crm.multitenant.tenant.persistence.repository.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.test.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findByPersonFirstName(String firstName);
	Optional<Employee> findByPersonLastName(String lastName);
	
	Boolean existsByPersonFirstName(String firstName);
	Boolean existsByPersonLastName(String lastName);
	
	
}