package com.pfi.crm.payload.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PersonaFisicaAbstractRequest extends ContactoAbstractRequest{
	
	
	private int dni;
	
	@NotBlank
    @Size(max = 140)
	private String nombre;
	
	@NotBlank
    @Size(max = 140)
	private String apellido;
	
	private LocalDate fechaNacimiento;
}
