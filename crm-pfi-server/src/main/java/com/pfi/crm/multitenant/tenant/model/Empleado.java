package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;

@Entity
@Table(name="empleado")
public class Empleado extends TrabajadorAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmpleado;
	
	private String funcion;
	private String descripcion;
	private boolean estadoActivoEmpleado;
	
	//Constructor
	public Empleado() {
		super();
		this.estadoActivoEmpleado = true;
	}
	
	public Empleado(Long idEmpleado, String funcion, String descripcion) {
		super();
		this.setFechaAltaContacto(LocalDate.now());
		this.setEstadoActivoPersonaFisica(true);
		this.setEstadoActivoEmpleado(true);
		this.idEmpleado = idEmpleado;
		this.funcion = funcion;
		this.descripcion = descripcion;
		this.estadoActivoEmpleado = true;
	}
	
	public Empleado(EmpleadoPayload p) {
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
		
		// TrabajadorAbstract
		this.setDatosBancarios(p.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Empleado
		this.setIdEmpleado(null);
		this.setFuncion(p.getFuncion());
		this.setDescripcion(p.getDescripcion());
		this.setEstadoActivoEmpleado(true);
		// Fin Empleado
	}
	
	
	
	//Getters and Setters
	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean getEstadoActivoEmpleado() {
		return estadoActivoEmpleado;
	}

	public void setEstadoActivoEmpleado(boolean estadoActivoEmpleado) {
		this.estadoActivoEmpleado = estadoActivoEmpleado;
	}
	
	
	
	
	
	
	public EmpleadoPayload toPayload() {
		EmpleadoPayload p = new EmpleadoPayload();
		
		// Contacto
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto
		
		// Persona Juridica
			// p.setIdPersonaFisica(null);
		p.setDni(this.getDni());
		p.setNombre(this.getNombre());
		p.setApellido(this.getApellido());
		p.setFechaNacimiento(this.getFechaNacimiento());
		//p.setEstadoActivoPersonaFisica(this.getEstadoActivoPersonaFisica());
		// Fin Persona Juridica
		
		// TrabajadorAbstract
		p.setDatosBancarios(this.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Empleado
		//this.setIdEmpleado(null);
		p.setFuncion(this.getFuncion());
		p.setDescripcion(this.getDescripcion());
		//this.setEstadoActivoEmpleado(true);
		// Fin Empleado
		
		return p;
	}
	
	
	public void modificar(EmpleadoPayload p) {
		
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
		//this.setEstadoActivoVoluntario(true);
		// Fin Persona Fisica
		
		// TrabajadorAbstract
		this.setDatosBancarios(p.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Empleado
		//this.setIdEmpleado(null);
		this.setFuncion(p.getFuncion());
		this.setDescripcion(p.getDescripcion());
		//this.setEstadoActivoEmpleado(true);
		// Fin Empleado
	}
}
