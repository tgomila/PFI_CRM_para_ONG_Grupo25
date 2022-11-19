package com.pfi.crm.multitenant.tenant.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfi.crm.multitenant.tenant.model.test.Customer;
import com.pfi.crm.multitenant.tenant.model.test.Employee;
import com.pfi.crm.multitenant.tenant.model.test.Person;
import com.pfi.crm.multitenant.tenant.repository.test.CustomerRepository;
import com.pfi.crm.multitenant.tenant.repository.test.EmployeeRepository;
import com.pfi.crm.multitenant.tenant.repository.test.PersonRepository;

@Service
public class zEjemploService {
	
	@Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(zEjemploService.class);
    
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
