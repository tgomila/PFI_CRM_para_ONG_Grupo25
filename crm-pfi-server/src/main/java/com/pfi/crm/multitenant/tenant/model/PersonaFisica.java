package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaAbstractPayload;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;

@Entity
@Table(name ="personaFisica")
public class PersonaFisica extends ContactoAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaFisica;
	
	private int dni;
	private String nombre;
	private String apellido;
	private LocalDate fechaNacimiento;
	private boolean estadoActivoPersonaFisica;
	//private int edad;	//Necesario sabiendo la fecha de nacimiento?
	
	//Metodo edad
	public int getEdad () {
        if (fechaNacimiento != null) {
            return Period.between(fechaNacimiento, LocalDate.now()).getYears();
        } else {
            return -1;
        }
    }
	
	//Constructores
	public PersonaFisica() {
		super();
		this.setIdPersonaFisica(null);
		this.setEstadoActivoPersonaFisica(true);
	}
	
	public PersonaFisica(PersonaFisicaAbstractPayload ap) {
		super();
		this.modificar(ap); //Setear contacto y persona
		this.setIdPersonaFisica(null);
		this.setEstadoActivoPersonaFisica(true);
		// Fin Persona Fisica
	}
	
	

	public Long getIdPersonaFisica() {
		return idPersonaFisica;
	}

	public void setIdPersonaFisica(Long idPersonaFisica) {
		this.idPersonaFisica = idPersonaFisica;
	}

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public boolean getEstadoActivoPersonaFisica() {
		return estadoActivoPersonaFisica;
	}

	public void setEstadoActivoPersonaFisica(boolean estadoActivoPersonaFisica) {
		this.estadoActivoPersonaFisica = estadoActivoPersonaFisica;
	}
	
	
	
	
	

	public PersonaFisicaPayload toPayload() {
		
		PersonaFisicaPayload p = new PersonaFisicaPayload();
		
		// Contacto
		p.modificarContacto(this.toContactoPayload());
		
		// Persona Fisica
		p.setDni(this.getDni());
		p.setNombre(this.getNombre());
		p.setApellido(this.getApellido());
		p.setFechaNacimiento(this.getFechaNacimiento());
		// Fin Persona Fisica
		
		return p;
	}
	
	public void modificar(PersonaFisicaAbstractPayload p) {
		
		// Contacto
		this.modificarContacto(p);
		
		// Persona Fisica
		this.setDni(p.getDni());
		this.setNombre(p.getNombre());
		this.setApellido(p.getApellido());
		this.setFechaNacimiento(p.getFechaNacimiento());
		// Fin Persona Fisica
	}
}
