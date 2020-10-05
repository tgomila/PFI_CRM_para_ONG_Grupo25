package com.pfi.crm.model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="personaFisica")
public class PersonaFisica extends ContactoAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaJuridica;
	
	private int dni;
	private String nombre;
	private String apellido;
	private LocalDate fechaNacimiento;
	//private int edad;	//Necesario sabiendo la fecha de nacimiento?
	
	//Metodo edad
	public int getEdad () {
        if (fechaNacimiento != null) {
            return Period.between(fechaNacimiento, LocalDate.now()).getYears();
        } else {
            return 0;
        }
    }
	
	//Constructores
	public PersonaFisica() {
		super();
	}

	public PersonaFisica(Long idPersonaJuridica, int dni, String nombre, String apellido, LocalDate fechaNacimiento) {
		super();
		this.idPersonaJuridica = idPersonaJuridica;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
	}

	public Long getIdPersonaJuridica() {
		return idPersonaJuridica;
	}

	public void setIdPersonaJuridica(Long idPersonaJuridica) {
		this.idPersonaJuridica = idPersonaJuridica;
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
	
	
	
	
	
	
}
