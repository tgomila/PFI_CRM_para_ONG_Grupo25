package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.BeneficiarioPayload;

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
	private boolean seRetiraSolo = false;
	private String cuidadosEspeciales;
	private String escuela;
	private String grado;
	private String turno;
	private boolean estadoActivoBeneficiario = true;
	
	//Constructor
	public Beneficiario() {
		super();
		this.setIdBeneficiario(null);
		this.setEstadoActivoBeneficiario(true);
		this.setSeRetiraSolo(false);	//Inicializado
	}
	
	public Beneficiario(BeneficiarioPayload payload) {
		super();
		this.modificar(payload);
		this.setIdBeneficiario(null);
		this.setEstadoActivoBeneficiario(true);
	}
	
	public void modificar(BeneficiarioPayload payload) {
		this.modificarPersonaFisica(payload);
		this.setIdONG(payload.getIdONG());
		this.setLegajo(payload.getLegajo());
		this.setLugarDeNacimiento(payload.getLugarDeNacimiento());
		this.setSeRetiraSolo(payload.getSeRetiraSolo());
		this.setCuidadosEspeciales(payload.getCuidadosEspeciales());
		this.setEscuela(payload.getEscuela());
		this.setGrado(payload.getGrado());
		this.setTurno(payload.getTurno());
	}
	
	public BeneficiarioPayload toPayload() {
		BeneficiarioPayload payload = new BeneficiarioPayload();
		payload.modificarPersonaFisica(this.toPersonaFisicaPayload());
		payload.setIdONG(this.getIdONG());
		payload.setLegajo(this.getLegajo());
		payload.setLugarDeNacimiento(this.getLugarDeNacimiento());
		payload.setSeRetiraSolo(this.getSeRetiraSolo());
		payload.setCuidadosEspeciales(this.getCuidadosEspeciales());
		payload.setEscuela(this.getEscuela());
		payload.setGrado(this.getGrado());
		payload.setTurno(this.getTurno());
		return payload;
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
}
