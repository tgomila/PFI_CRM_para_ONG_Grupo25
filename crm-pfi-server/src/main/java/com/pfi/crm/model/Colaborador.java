package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.ColaboradorPayload;

@Entity
@Table(name ="colaborador")
public class Colaborador extends TrabajadorAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idColaborador;
	
	private String area;
	
	private boolean estadoActivoColaborador;
	
	
	//Constructor
	public Colaborador() {
		super();
		estadoActivoColaborador = true;
	}

	public Colaborador(Long idColaborador, String area) {
		super();
		this.idColaborador = idColaborador;
		this.area = area;
		estadoActivoColaborador = true;
	}
	
	
	public Colaborador(ColaboradorPayload p) {
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
		
		// Colaborador
		this.setIdColaborador(null);
		this.setArea(p.getArea());
		this.setEstadoActivoColaborador(true);
		// Fin Colaborador
	}
	
	
	
	
	//Getters and Setters
	public Long getIdColaborador() {
		return idColaborador;
	}

	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public boolean getEstadoActivoColaborador() {
		return estadoActivoColaborador;
	}

	public void setEstadoActivoColaborador(boolean estadoActivoColaborador) {
		this.estadoActivoColaborador = estadoActivoColaborador;
	}
	
	
	
	
	
	
	public ColaboradorPayload toPayload() {
		ColaboradorPayload p = new ColaboradorPayload();
		
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
		
		// Colaborador
		//p.setIdColaborador(this.getIdColaborador());
		p.setArea(this.getArea());
		//p.setEstadoActivoColaborador(this.getEstadoActivoColaborador());
		// Fin Colaborador
		
		return p;
	}
	
	
	public void modificar(ColaboradorPayload p) {
		
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
		
		// Colaborador
		//this.setIdColaborador(null);
		this.setArea(p.getArea());
		//this.setEstadoActivoColaborador(true);
		// Fin Colaborador
	}
	
}
