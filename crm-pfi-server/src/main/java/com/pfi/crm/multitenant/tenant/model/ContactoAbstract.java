package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ContactoAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;

@MappedSuperclass
public abstract class ContactoAbstract extends UserDateAudit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = {CascadeType.MERGE} )
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
	
	public ContactoPayload toContactoPayload() {
		return this.contacto.toPayload();
	}
	
	public void modificarContacto(ContactoAbstractPayload p) {
		this.contacto.modificar(p);
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
