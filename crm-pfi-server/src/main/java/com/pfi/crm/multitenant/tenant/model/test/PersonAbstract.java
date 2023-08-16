package com.pfi.crm.multitenant.tenant.model.test;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class PersonAbstract{
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_abstract_seq")
	@SequenceGenerator(name = "person_abstract_seq", sequenceName = "person_abstract_sequence", allocationSize = 1)
	private long id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("firstName ASC")
	private Person person;
	
	
	//Constructores
	public PersonAbstract() {
		super();
		person = new Person();
	}
	
	public PersonAbstract(long id, String firstName, String lastName) {
		super();
		person = new Person(id, firstName, lastName);
	}
	
	public PersonAbstract(Person person) {
		super();
		this.person = person;
	}
	
	
	//Getters and Setters
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	
	
	
	//Getters y Setters de Person.java
	public long getId() {
		return person.getId();
	}

	public void setId(long id) {
		person.setId(id);
	}

	public String getFirstName() {
		return person.getFirstName();
	}

	public void setFirstName(String firstName) {
		person.setFirstName(firstName);
	}

	public String getLastName() {
		return person.getLastName();
	}

	public void setLastName(String lastName) {
		person.setLastName(lastName);
	}
	
	public boolean getEstaActivo() {
		return person.getEstaActivo();
	}

	public void setEstaActivo(boolean estaActivo) {
		person.setEstaActivo(estaActivo);
	}
	
}
