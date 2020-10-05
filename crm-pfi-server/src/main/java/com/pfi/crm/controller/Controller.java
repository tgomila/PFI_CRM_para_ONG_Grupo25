package com.pfi.crm.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.model.Contacto;
import com.pfi.crm.model.Voluntario;
import com.pfi.crm.model.test.Customer;
import com.pfi.crm.model.test.Employee;
import com.pfi.crm.model.test.Person;
import com.pfi.crm.repository.VoluntarioRepository;
import com.pfi.crm.repository.test.CustomerRepository;
import com.pfi.crm.repository.test.EmployeeRepository;
import com.pfi.crm.repository.test.PersonRepository;
//import com.pfi.crm.repository.PersonRepository;
import com.pfi.crm.service.EjemploService;

@RestController
@RequestMapping("/test")
public class Controller {
	
	@Autowired
    private PersonRepository personRepository;
	
	@Autowired
    private EjemploService ejemploService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VoluntarioRepository voluntarioRepository;
    
    @PostMapping("/testEntities")
    public Employee createEmployee(/*@Valid @RequestBody Employee employee*/) {
    	System.out.println("Entre aca");
    	
    	/*Person person = new Person();
    	//person.setId(1);
    	person.setFirstName("El fabuloso");
    	person.setLastName("Godofredo");
    	personRepository.save(person);*/
    	
    	Employee employee = new Employee();
    	//employee.setId(1);
    	employee.setFirstName("El fabuloso");
    	employee.setLastName("Godofredo");
    	employee.setPayrate(Double.parseDouble("47500.25"));
    	employee.setHireDate(new Date());
    	employeeRepository.save(employee);
    	
    	Employee employee2 = new Employee();
    	//employee.setId(1);
    	employee2.setFirstName("Sori");
    	employee2.setLastName("Titoli");
    	employee2.setPayrate(Double.parseDouble("74000.00"));
    	employee2.setHireDate(new Date());
    	employeeRepository.save(employee2);
    	
    	Person p = personRepository.findById(Long.parseLong("1")).orElse(new Person());
    	Customer customer = new Customer();
    	customer.setPerson(p);
    	customer.setFirstName("MODIFICO");
    	customer.setRating("SILVER");
    	customerRepository.save(customer);
    	
    	return employee;
    }
    
    @PostMapping("/testVoluntario")
    public Voluntario altaVoluntarioTest(/*@Valid @RequestBody Voluntario voluntario*/) {
    	System.out.println("Entre aca");
    	
    	Contacto c = new Contacto();
    	c.setEstadoActivoContacto(true);
    	c.setNombreDescripcion("Voluntario Don Roque");
    	c.setCuit("20-1235678-9");
    	c.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
    	c.setEmail("felipe@gmail.com");
    	c.setTelefono("1234-4567");
    	Voluntario v = new Voluntario();
    	v.setContacto(c);
    	
    	Voluntario ret = voluntarioRepository.save(v);
    	
    	return ret;
    }
    
    //MiniCambio
    @PostMapping("/voluntario")
    public Voluntario altaVoluntario(@Valid @RequestBody Voluntario voluntario) {
    	System.out.println("Entre aca");
    	
    	return voluntarioRepository.save(voluntario);
    }
}
