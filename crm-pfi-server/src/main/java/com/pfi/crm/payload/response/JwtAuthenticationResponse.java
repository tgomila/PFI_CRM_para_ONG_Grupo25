package com.pfi.crm.payload.response;

import java.util.List;

import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;

public class JwtAuthenticationResponse {
	
	private String userName;
	private String token;
	private String tokenType = "Bearer";
	private List<String> roles;
	private Integer tenantClientId;
	private String dbName;
	private String tenantName;
	private ContactoPayload contacto;
	private PersonaFisicaPayload persona;
	private EmpleadoPayload empleado;
	private ProfesionalPayload profesional;

	public JwtAuthenticationResponse(String userName, String token, List<String> roles, Integer tenantClientId, String dbName, String tenantName,
			ContactoPayload contacto, PersonaFisicaPayload persona, EmpleadoPayload empleado, ProfesionalPayload profesional) {
		this.userName = userName;
		this.token = token;
		this.roles = roles;
		this.tenantClientId = tenantClientId;
		this.dbName = dbName;
		this.tenantName = tenantName;
		this.contacto = contacto;
		this.persona = persona;
		this.empleado = empleado;
		this.profesional = profesional;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Integer getTenantClientId() {
		return tenantClientId;
	}

	public void setTenantClientId(Integer tenantClientId) {
		this.tenantClientId = tenantClientId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public ContactoPayload getContacto() {
		return contacto;
	}

	public void setContacto(ContactoPayload contacto) {
		this.contacto = contacto;
	}

	public PersonaFisicaPayload getPersona() {
		return persona;
	}

	public void setPersona(PersonaFisicaPayload persona) {
		this.persona = persona;
	}

	public EmpleadoPayload getEmpleado() {
		return empleado;
	}

	public void setEmpleado(EmpleadoPayload empleado) {
		this.empleado = empleado;
	}

	public ProfesionalPayload getProfesional() {
		return profesional;
	}

	public void setProfesional(ProfesionalPayload profesional) {
		this.profesional = profesional;
	}
	
	
}
