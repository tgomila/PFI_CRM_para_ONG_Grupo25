package com.pfi.crm.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfi.crm.model.test.Customer;
import com.pfi.crm.model.test.Employee;
import com.pfi.crm.model.test.Person;
import com.pfi.crm.repository.test.CustomerRepository;
import com.pfi.crm.repository.test.EmployeeRepository;
import com.pfi.crm.repository.test.PersonRepository;

@Service
public class EjemploService {
	
	@Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(EjemploService.class);
    
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }
    
    public Optional<Person> findPersonById(Long id) {
        return personRepository.findById(id);
    }
    
    
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }
}
