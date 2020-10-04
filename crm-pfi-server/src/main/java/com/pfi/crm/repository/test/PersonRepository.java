package com.pfi.crm.repository.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.model.test.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
	Optional<Person> findByFirstName(String firstName);
	Optional<Person> findByLastName(String lastName);
	
    Boolean existsByFirstName(String firstName);
    Boolean existsByLastName(String lastName);
}
