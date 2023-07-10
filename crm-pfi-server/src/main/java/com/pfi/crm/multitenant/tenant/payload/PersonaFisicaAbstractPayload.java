package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;
import java.time.Period;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonaFisicaAbstractPayload extends ContactoAbstractPayload{
	
	
	private int dni;
	
	@NotBlank
    @Size(max = 140)
	private String nombre;
	
	@NotBlank
    @Size(max = 140)
	private String apellido;
	
	private LocalDate fechaNacimiento;
	
	private Integer edad = null;
	
	
	public PersonaFisicaAbstractPayload modificarPersonaFisica(PersonaFisicaAbstractPayload payload) {
		this.modificarContacto(payload);
		this.setDni(payload.getDni());
		this.setNombre(payload.getNombre());
		this.setApellido(payload.getApellido());
		this.setFechaNacimiento(payload.getFechaNacimiento());
		return this;
	}
	
	public PersonaFisicaPayload toPersonaFisicaPayload() {
		PersonaFisicaPayload payload = new PersonaFisicaPayload();
		payload.modificarPersonaFisica(this);
		return payload;
	}
	
	
	//Getters and Setters
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
		if (fechaNacimiento != null)
            edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
		else
			edad = null;
	}
	
	public Integer getEdad() {
		return edad;
	}
	
}
