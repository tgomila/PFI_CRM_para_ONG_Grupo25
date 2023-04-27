package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;

@MappedSuperclass
public abstract class PersonaFisicaAbstract extends UserDateAudit /*extends ContactoAbstract*/{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	private PersonaFisica personaFisica;
	
	public PersonaFisicaAbstract() {
		super();
		personaFisica = new PersonaFisica();
	}

	
	public PersonaFisicaPayload toPersonaFisicaPayload() {
		return this.personaFisica.toPayload();
	}
	
	public void modificarPersonaFisica(PersonaFisicaAbstractPayload payload) {
		this.personaFisica.modificar(payload);
	}

	//metodo agregado
	public Contacto getContacto() {
		return personaFisica.getContacto();
	}

	public void setContacto(Contacto contacto) {
		personaFisica.setContacto(contacto);
	}
	
	
	public int getEdad() {
		return personaFisica.getEdad();
	}


	public PersonaFisicaAbstract(PersonaFisica personaFisica) {
		super();
		this.personaFisica = personaFisica;
	}


	//Getters and Setters
	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}
	
	public Long getIdPersonaFisica() {
		return personaFisica.getIdPersonaFisica();
	}

	public void setIdPersonaFisica(Long idPersonaFisica) {
		personaFisica.setIdPersonaFisica(idPersonaFisica);
	}

	public int getDni() {
		return personaFisica.getDni();
	}

	public void setDni(int dni) {
		personaFisica.setDni(dni);
	}

	public String getNombre() {
		return personaFisica.getNombre();
	}

	public void setNombre(String nombre) {
		personaFisica.setNombre(nombre);
	}

	public String getApellido() {
		return personaFisica.getApellido();
	}

	public void setApellido(String apellido) {
		personaFisica.setApellido(apellido);
	}

	public LocalDate getFechaNacimiento() {
		return personaFisica.getFechaNacimiento();
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		personaFisica.setFechaNacimiento(fechaNacimiento);
	}
	
	public boolean getEstadoActivoPersonaFisica() {
		return personaFisica.getEstadoActivoPersonaFisica();
	}

	public void setEstadoActivoPersonaFisica(boolean estadoActivoPersonaFisica) {
		personaFisica.setEstadoActivoPersonaFisica(estadoActivoPersonaFisica);
	}
	
	
	
	
	
	
	
	
	
	
	// Getters y Setters de Contacto.java, actualizar si es necesario
	public Long getId() {
		return personaFisica.getId();
	}

	public void setId(Long id) {
		this.personaFisica.setId(id);
	}

	public boolean getEstadoActivoContacto() {
		return personaFisica.getEstadoActivoContacto();
	}

	public void setEstadoActivoContacto(boolean estadoActivoContacto) {
		personaFisica.setEstadoActivoContacto(estadoActivoContacto);
	}

	public LocalDate getFechaAltaContacto() {
		return personaFisica.getFechaAltaContacto();
	}

	public void setFechaAltaContacto(LocalDate fechaAltaContacto) {
		personaFisica.setFechaAltaContacto(fechaAltaContacto);
	}

	public LocalDate getFechaBajaContacto() {
		return personaFisica.getFechaBajaContacto();
	}

	public void setFechaBajaContacto(LocalDate fechaBajaContacto) {
		personaFisica.setFechaBajaContacto(fechaBajaContacto);
	}

	public String getNombreDescripcion() {
		return personaFisica.getNombreDescripcion();
	}

	public void setNombreDescripcion(String nombreDescripcion) {
		personaFisica.setNombreDescripcion(nombreDescripcion);
	}

	public String getCuit() {
		return personaFisica.getCuit();
	}

	public void setCuit(String cuit) {
		personaFisica.setCuit(cuit);
	}

	public String getDomicilio() {
		return personaFisica.getDomicilio();
	}

	public void setDomicilio(String domicilio) {
		personaFisica.setDomicilio(domicilio);
	}

	public String getEmail() {
		return personaFisica.getEmail();
	}

	public void setEmail(String email) {
		personaFisica.setEmail(email);
	}

	public String getTelefono() {
		return personaFisica.getTelefono();
	}

	public void setTelefono(String telefono) {
		personaFisica.setTelefono(telefono);
	}
}
