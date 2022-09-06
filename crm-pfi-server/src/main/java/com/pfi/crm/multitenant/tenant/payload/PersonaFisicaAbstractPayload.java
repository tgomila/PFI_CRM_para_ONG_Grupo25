package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;

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
