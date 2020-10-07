package com.pfi.crm.model;

import java.time.LocalDate;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.PersonaFisicaPayload;

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
            return 0;
        }
    }
	
	//Constructores
	public PersonaFisica() {
		super();
	}

	public PersonaFisica(Long idPersonaFisica, int dni, String nombre, String apellido, LocalDate fechaNacimiento) {
		super();
		this.idPersonaFisica = idPersonaFisica;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.estadoActivoPersonaFisica = true;
	}
	
	public PersonaFisica(PersonaFisicaPayload p) {
		super();
		// Contacto
		this.setId(p.getId());
		this.setEstadoActivoContacto(true);
		this.setFechaAltaContacto(LocalDate.now());
		this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		// Fin Contacto
		
		// Persona Fisica
		this.setIdPersonaFisica(null);
		this.setDni(p.getDni());
		this.setNombre(p.getNombre());
		this.setApellido(p.getApellido());
		this.setFechaNacimiento(p.getFechaNacimiento());
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
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto
		
		// Persona Juridica
			//p.setIdPersonaFisica(null);
		p.setDni(this.getDni());
		p.setNombre(this.getNombre());
		p.setApellido(this.getApellido());
		p.setFechaNacimiento(this.getFechaNacimiento());
		//p.setEstadoActivoPersonaFisica(this.getEstadoActivoPersonaFisica());
		// Fin Persona Juridica
		
		
		return p;
	}
	
	public void modificar(PersonaFisicaPayload p) {
		
		// Contacto
		//this.setId(p.getId());
		//this.setEstadoActivoContacto(true);
		//this.setFechaAltaContacto(LocalDate.now());
		//this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		// Fin Contacto
		
		// Persona Fisica
		this.setDni(p.getDni());
		this.setNombre(p.getNombre());
		this.setApellido(p.getApellido());
		this.setFechaNacimiento(p.getFechaNacimiento());
		//this.setEstadoActivoPersonaFisica(true);
		// Fin Persona Fisica
	}
}
