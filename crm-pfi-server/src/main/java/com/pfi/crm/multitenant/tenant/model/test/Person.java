package com.pfi.crm.multitenant.tenant.model.test;

import javax.persistence.*;

import com.pfi.crm.multitenant.tenant.model.audit.DateAudit;

@Entity @Table(name="zTEST_PERSON")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@MappedSuperclass
public class Person extends DateAudit{

	/**
	 * 
	 */
	private static final long serialVersionUID = 351444627902533075L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
	@SequenceGenerator(name = "person_seq", sequenceName = "person_sequence", allocationSize = 1)
	private long id;

	private String firstName;

	private String lastName;
	
	private boolean estaActivo;
	
	public Person() {
		super();
		estaActivo = true;
	}
	
	public Person(long id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		estaActivo = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean getEstaActivo() {
		return estaActivo;
	}

	public void setEstaActivo(boolean estaActivo) {
		this.estaActivo = estaActivo;
	}

}