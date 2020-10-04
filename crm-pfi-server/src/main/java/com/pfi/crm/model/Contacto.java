package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
		this.fechaAltaContacto = LocalDate.now(); //Fecha de hoy en server
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
	
	
	
}
