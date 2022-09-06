package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

@MappedSuperclass
public abstract class ContactoAbstract {
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private Contacto contacto;

	public ContactoAbstract() {
		super();
		contacto = new Contacto();
	}

	public Contacto getContacto() {
		return contacto;
	}

	public void setContacto(Contacto contacto) {
		this.contacto = contacto;
	}
	
	
	//Getters y Setters de Contacto.java, actualizar si es necesario
	public Long getId() {
		return contacto.getId();
	}
	public void setId(Long id) {
		this.contacto.setId(id);
	}
	public boolean getEstadoActivoContacto() {
		return contacto.getEstadoActivoContacto();
	}
	public void setEstadoActivoContacto(boolean estadoActivoContacto) {
		contacto.setEstadoActivoContacto(estadoActivoContacto);
	}
	public LocalDate getFechaAltaContacto() {
		return contacto.getFechaAltaContacto();
	}
	public void setFechaAltaContacto(LocalDate fechaAltaContacto) {
		contacto.setFechaAltaContacto(fechaAltaContacto);;
	}
	public LocalDate getFechaBajaContacto() {
		return contacto.getFechaBajaContacto();
	}
	public void setFechaBajaContacto(LocalDate fechaBajaContacto) {
		contacto.setFechaBajaContacto(fechaBajaContacto);
	}
	public String getNombreDescripcion() {
		return contacto.getNombreDescripcion();
	}
	public void setNombreDescripcion(String nombreDescripcion) {
		contacto.setNombreDescripcion(nombreDescripcion);
	}
	public String getCuit() {
		return contacto.getCuit();
	}
	public void setCuit(String cuit) {
		contacto.setCuit(cuit);
	}
	public String getDomicilio() {
		return contacto.getDomicilio();
	}
	public void setDomicilio(String domicilio) {
		contacto.setDomicilio(domicilio);
	}
	public String getEmail() {
		return contacto.getEmail();
	}
	public void setEmail(String email) {
		contacto.setEmail(email);
	}
	public String getTelefono() {
		return contacto.getTelefono();
	}
	public void setTelefono(String telefono) {
		contacto.setTelefono(telefono);
	}
}
