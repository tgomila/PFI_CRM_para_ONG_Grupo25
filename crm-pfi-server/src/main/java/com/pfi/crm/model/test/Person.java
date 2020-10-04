package com.pfi.crm.model.test;

import javax.persistence.*;

import com.pfi.crm.model.audit.DateAudit;

@Entity @Table(name="zTEST_PERSON")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@MappedSuperclass
public class Person extends DateAudit{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)

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
    
    