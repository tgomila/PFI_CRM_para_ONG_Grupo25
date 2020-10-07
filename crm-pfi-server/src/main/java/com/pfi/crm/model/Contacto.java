package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.ContactoPayload;

@Entity
@Table(name ="contacto")
public class Contacto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
		this.estadoActivoContacto = true;
		this.fechaAltaContacto = LocalDate.now(); //Fecha de hoy en server
	}
	
	public Contacto(ContactoPayload p) {
		super();
		this.setId(p.getId());
		this.setEstadoActivoContacto(true);
		this.setFechaAltaContacto(LocalDate.now());
		this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		
		/*this.id = p.getId();
		this.estadoActivoContacto = true;
		this.fechaAltaContacto = LocalDate.now();
		this.fechaBajaContacto = null;
		this.nombreDescripcion = p.getNombreDescripcion();
		this.cuit = p.getCuit();
		this.domicilio = p.getDomicilio();
		this.email = p.getEmail();
		this.telefono = p.getTelefono();*/
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
	}
	public LocalDate getFechaBajaContacto() {
		return fechaBajaContacto;
	}
	public void setFechaBajaContacto(LocalDate fechaBajaContacto) {
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
	/*public Contacto toModel(ContactoPayload p) {

		// Contacto
		Contacto m = new Contacto();
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());

		return m;
	}*/

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
}
