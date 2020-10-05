package com.pfi.crm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BeneficiarioRequest extends PersonaFisicaAbstractRequest{
	
	//private Long idBeneficiario;
		
	private Long idONG;
	
	private Long legajo;
	
	@NotBlank
    @Size(max = 140)
	private String lugarDeNacimiento;
	
	private boolean seRetiraSolo;
	
	@NotBlank
    @Size(max = 140)
	private String cuidadosEspeciales;
	
	@NotBlank
    @Size(max = 140)
	private String escuela;
	
	@NotBlank
    @Size(max = 30)
	private String grado;
	
	@NotBlank
    @Size(max = 30)
	private String turno;
	
	
	
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
