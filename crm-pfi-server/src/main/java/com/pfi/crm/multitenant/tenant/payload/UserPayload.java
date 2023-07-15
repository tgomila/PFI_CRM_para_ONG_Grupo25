package com.pfi.crm.multitenant.tenant.payload;

import java.util.HashSet;
import java.util.Set;

import com.pfi.crm.multitenant.tenant.model.RoleName;

public class UserPayload {
	
	private Long id;
	private String name;
	private String email;
	private ContactoPayload contacto;
	private Set<RoleName> roles = new HashSet<>();
	
	public UserPayload() {
		super();
	}

	public UserPayload(Long id, String name, String email, ContactoPayload contactoPayload, Set<RoleName> roles) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.contacto = contactoPayload;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactoPayload getContacto() {
		return contacto;
	}

	public void setContacto(ContactoPayload contacto) {
		this.contacto = contacto;
	}

	public Set<RoleName> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleName> roles) {
		this.roles = roles;
	}
}
