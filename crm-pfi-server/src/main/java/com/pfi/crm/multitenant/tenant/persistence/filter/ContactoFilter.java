package com.pfi.crm.multitenant.tenant.persistence.filter;

public class ContactoFilter {
	
	private Long id;
	private String nombreDescripcion;
	private String cuit;
	private String domicilio;
	private String email;
	private String telefono;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombreDescripcion() {
		return nombreDescripcion;
	}
	public void setNombreDescripcion(String nombreDescripcion) {
		this.nombreDescripcion = nombreDescripcion;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
}
