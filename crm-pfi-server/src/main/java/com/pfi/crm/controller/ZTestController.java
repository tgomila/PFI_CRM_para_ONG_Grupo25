package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Voluntario;
import com.pfi.crm.multitenant.tenant.model.test.Customer;
import com.pfi.crm.multitenant.tenant.model.test.Employee;
import com.pfi.crm.multitenant.tenant.model.test.Person;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.VoluntarioRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.test.CustomerRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.test.EmployeeRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.test.PersonRepository;
import com.pfi.crm.multitenant.tenant.service.ContactoService;

//Se usa para "test conexión" para ver si un login token esta expirado, abajo de todo
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ZTestController {
	
	@Autowired
    private PersonRepository personRepository;
	
	@Autowired
    private ContactoService contactoService;
	
	//@Autowired
    //private EjemploService ejemploService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private VoluntarioRepository voluntarioRepository;
    
    @GetMapping("/testContacto")
    public ContactoPayload getContacto(/*@Valid @RequestBody Contacto contacto*/) {
    	System.out.println("Entre aca");
    	
    	Contacto c = new Contacto();
    	
    	//Contacto
    	c.setEstadoActivoContacto(true);
    	c.setNombreDescripcion("Contacto test");
    	c.setCuit("20-1235678-9");
    	c.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
    	c.setEmail("testContacto@gmail.com");
    	c.setTelefono("1234-4567");
    	
    	return c.toPayload();
    }
    
    @PostMapping("/testContacto")
    public ContactoPayload getContacto(@Valid @RequestBody ContactoPayload payload) {
    	return contactoService.altaContacto(new Contacto(payload)).toPayload();
    }
    
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
    	
    	Voluntario v = new Voluntario();
    	
    	//Contacto
    	v.setEstadoActivoContacto(true);
    	v.setNombreDescripcion("Voluntario Don Roque");
    	v.setCuit("20-1235678-9");
    	v.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
    	v.setEmail("felipe@gmail.com");
    	v.setTelefono("1234-4567");
    	
    	//PersonaFisica
    	v.setNombre("Felipe");
    	v.setApellido("del 8");
    	v.setFechaNacimiento(LocalDate.of(1990, 1, 20));
    	
    	Voluntario ret = voluntarioRepository.save(v);
    	
    	return ret;
    }
    
    //MiniCambio
    @PostMapping("/voluntario")
    public Voluntario altaVoluntario(@Valid @RequestBody Voluntario voluntario) {
    	System.out.println("Entre aca");
    	
    	return voluntarioRepository.save(voluntario);
    }
    
    
    
    
    //test conexión
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public String userAccess() {
		return "User Content.";
	}
	
	@GetMapping("/profesional")
	@PreAuthorize("hasRole('ROLE_PROFESIONAL')")
	public String profesionalAccess() {
		return "Profesional Board.";
	}
	
	@GetMapping("/employee")
	@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public String employeeAccess() {
		return "Employee Board.";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
