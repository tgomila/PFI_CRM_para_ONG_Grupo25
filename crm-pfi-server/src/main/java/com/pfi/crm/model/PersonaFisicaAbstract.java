package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersonaFisicaAbstract extends ContactoAbstract{
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	private PersonaFisica personaFisica;
	
	
	
	public PersonaFisica getPersonaFisica() {
		return personaFisica;
	}

	public void setPersonaFisica(PersonaFisica personaFisica) {
		this.personaFisica = personaFisica;
	}

	//metodo agregado
	public int getEdad() {
		return personaFisica.getEdad();
	}
	
	
	public PersonaFisicaAbstract() {
		super();
		personaFisica = new PersonaFisica();
	}


	public PersonaFisicaAbstract(PersonaFisica personaFisica) {
		super();
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
	
	
}
