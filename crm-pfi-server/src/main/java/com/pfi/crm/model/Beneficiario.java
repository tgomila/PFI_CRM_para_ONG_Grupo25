package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * beneficiario: Persona que se beneficia del accionar de la ONG
 */

@Entity
@Table(name="beneficiario")
public class Beneficiario extends ContactoAbstract {
	
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
	
	//Constructor
	public Beneficiario() {
		super();
		seRetiraSolo = false;	//Inicializo por las dudas
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

	public boolean isSeRetiraSolo() {
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
	
	
}
