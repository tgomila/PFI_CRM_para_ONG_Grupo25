package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ContactoAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;

@Entity
@Table(name ="contacto")
public class Contacto extends UserDateAudit{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 218392515575775555L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contacto_seq")
	//@SequenceGenerator(name = "contacto_seq", sequenceName = "contacto_sequence", allocationSize = 1)
	private Long id;
	
	private boolean estadoActivoContacto;
	private LocalDate fechaAltaContacto;
	private LocalDate fechaBajaContacto;
	private String nombreDescripcion;
	
	private String cuit;
	private String domicilio;
	private String email;
	private String telefono;
	
	
	//Constructor
	public Contacto() {
		super();
		this.id = null;
		this.estadoActivoContacto = true;
		this.fechaAltaContacto = LocalDate.now(); //Fecha de hoy en server
		this.fechaBajaContacto = null;
	}
	
	public Contacto(ContactoAbstractPayload p) {
		super();
		this.modificar(p);
		this.setEstadoActivoContacto(true);
		this.setFechaAltaContacto(LocalDate.now());
		this.setFechaBajaContacto(null);
	}

	//Getters y Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean getEstadoActivoContacto() {
		return estadoActivoContacto;
	}
	public void setEstadoActivoContacto(boolean estadoActivoContacto) {
		this.estadoActivoContacto = estadoActivoContacto;
	}
	public LocalDate getFechaAltaContacto() {
		return fechaAltaContacto;
	}
	public void setFechaAltaContacto(LocalDate fechaAltaContacto) {
		this.fechaAltaContacto = fechaAltaContacto;
		if(this.getFechaBajaContacto() != null && fechaAltaContacto.isAfter(this.getFechaBajaContacto()))
			this.setFechaBajaContacto(null);
	}
	public LocalDate getFechaBajaContacto() {
		return fechaBajaContacto;
	}
	public void setFechaBajaContacto(LocalDate fechaBajaContacto) {
		if(fechaBajaContacto == null)//Null es activo
			this.estadoActivoContacto = true;
		else
			this.estadoActivoContacto = false;
		this.fechaBajaContacto = fechaBajaContacto;
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
	
	
	// Payloads
	public ContactoPayload toPayload() {

		ContactoPayload p = new ContactoPayload();

		// Contacto
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto

		return p;
	}
	
	public void modificar(ContactoAbstractPayload p) {
		// Contacto
		this.setId(p.getId());
		//this.setEstadoActivoContacto(true);
		//this.setFechaAltaContacto(LocalDate.now());
		//this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		// Fin Contacto
	}
	
	
}
