package com.pfi.crm.payload.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.*;

import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;

public class SignUpRequest {
	@NotBlank
	@Size(min = 4, max = 40)
	private String name;

	@NotBlank
	@Size(min = 3, max = 15)
	private String username;

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;
	
	private Integer tenantOrClientId;
	
	//Esto es si lo da de alta un usuario con permisos altos, no es necesario su instancia.
	private ContactoPayload contacto;
	private Set<RoleName> roles = new HashSet<>();
	//Fin si lo da de alta un usuario con permisos altos

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getTenantOrClientId() {
		return tenantOrClientId;
	}

	public void setTenantOrClientId(Integer tenantOrClientId) {
		this.tenantOrClientId = tenantOrClientId;
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
