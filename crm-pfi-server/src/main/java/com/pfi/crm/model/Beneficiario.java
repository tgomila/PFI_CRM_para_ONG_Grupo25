package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.BeneficiarioPayload;

/*
 * beneficiario: Persona que se beneficia del accionar de la ONG
 */

@Entity
@Table(name="beneficiario")
public class Beneficiario extends PersonaFisicaAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idBeneficiario;
	
	private Long idONG;
	private Long legajo;
	private String lugarDeNacimiento;
	private boolean seRetiraSolo;
	private String cuidadosEspeciales;
	private String escuela;
	private String grado;
	private String turno;
	private boolean estadoActivoBeneficiario;
	
	//Constructor
	public Beneficiario() {
		super();
		seRetiraSolo = false;	//Inicializo por las dudas
		estadoActivoBeneficiario = true;
	}
	
	public Beneficiario(Long idBeneficiario, Long idONG, Long legajo, String lugarDeNacimiento, boolean seRetiraSolo,
			String cuidadosEspeciales, String escuela, String grado, String turno) {
		super();
		this.idBeneficiario = idBeneficiario;
		this.idONG = idONG;
		this.legajo = legajo;
		this.lugarDeNacimiento = lugarDeNacimiento;
		this.seRetiraSolo = seRetiraSolo;
		this.cuidadosEspeciales = cuidadosEspeciales;
		this.escuela = escuela;
		this.grado = grado;
		this.turno = turno;
		this.estadoActivoBeneficiario = true;
	}
	
	public Beneficiario(BeneficiarioPayload p) {
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
		
		// Beneficiario
		this.setIdBeneficiario(null);
		this.setIdONG(p.getIdONG());
		this.setLegajo(p.getLegajo());
		this.setLugarDeNacimiento(p.getLugarDeNacimiento());
		this.setSeRetiraSolo(p.getSeRetiraSolo());
		this.setCuidadosEspeciales(p.getCuidadosEspeciales());
		this.setEscuela(p.getEscuela());
		this.setGrado(p.getGrado());
		this.setTurno(p.getTurno());
		this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
	}
	
	
	
	
	
	//Getters and Setters
	public Long getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(Long idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public Long getIdONG() {
		return idONG;
	}

	public void setIdONG(Long idONG) {
		this.idONG = idONG;
	}

	public Long getLegajo() {
		return legajo;
	}

	public void setLegajo(Long legajo) {
		this.legajo = legajo;
	}

	public String getLugarDeNacimiento() {
		return lugarDeNacimiento;
	}

	public void setLugarDeNacimiento(String lugarDeNacimiento) {
		this.lugarDeNacimiento = lugarDeNacimiento;
	}

	public boolean getSeRetiraSolo() {
		return seRetiraSolo;
	}

	public void setSeRetiraSolo(boolean seRetiraSolo) {
		this.seRetiraSolo = seRetiraSolo;
	}

	public String getCuidadosEspeciales() {
		return cuidadosEspeciales;
	}

	public void setCuidadosEspeciales(String cuidadosEspeciales) {
		this.cuidadosEspeciales = cuidadosEspeciales;
	}

	public String getEscuela() {
		return escuela;
	}

	public void setEscuela(String escuela) {
		this.escuela = escuela;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public boolean getEstadoActivoBeneficiario() {
		return estadoActivoBeneficiario;
	}

	public void setEstadoActivoBeneficiario(boolean estadoActivoBeneficiario) {
		this.estadoActivoBeneficiario = estadoActivoBeneficiario;
	}
	
	
	
	
	
	
	
	public BeneficiarioPayload toPayload() {
		BeneficiarioPayload p = new BeneficiarioPayload();
		
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
		
		// Beneficiario
		p.setIdONG(this.getIdONG());
		p.setLegajo(this.getLegajo());
		p.setLugarDeNacimiento(this.getLugarDeNacimiento());
		p.setSeRetiraSolo(this.getSeRetiraSolo());
		p.setCuidadosEspeciales(this.getCuidadosEspeciales());
		p.setEscuela(this.getEscuela());
		p.setGrado(this.getGrado());
		p.setTurno(this.getTurno());
		//p.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
		
		return p;
	}
	
	
	public void modificar(BeneficiarioPayload p) {
		
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
		
		// Beneficiario
		this.setIdONG(p.getIdONG());
		this.setLegajo(p.getLegajo());
		this.setLugarDeNacimiento(p.getLugarDeNacimiento());
		this.setSeRetiraSolo(p.getSeRetiraSolo());
		this.setCuidadosEspeciales(p.getCuidadosEspeciales());
		this.setEscuela(p.getEscuela());
		this.setGrado(p.getGrado());
		this.setTurno(p.getTurno());
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario
	}
	
}
