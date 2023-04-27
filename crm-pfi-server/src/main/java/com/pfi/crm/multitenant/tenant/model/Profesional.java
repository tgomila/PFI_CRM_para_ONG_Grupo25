package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;


@Entity
@Table(name ="profesional")
public class Profesional extends TrabajadorAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -32758458147602560L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProfesional;
	
	private String profesion;
	
	private boolean estadoActivoProfesional;
	
	//Constructores
	public Profesional() {
		super();
		this.setIdProfesional(null);
		this.setEstadoActivoProfesional(true);
	}
	
	public Profesional(ProfesionalPayload p) {
		super();
		
		this.modificar(p);
		this.setIdProfesional(null);
		this.setEstadoActivoProfesional(true);
	}
	
	public void modificar(ProfesionalPayload payload) {
		this.modificarTrabajador(payload);
		this.setProfesion(payload.getProfesion());
	}
	
	public ProfesionalPayload toPayload() {
		ProfesionalPayload payload = new ProfesionalPayload();
		payload.modificarTrabajadorPayload(this.toTrabajadorAbstractPayload());
		payload.setProfesion(this.getProfesion());
		return payload;
	}
	
	
	//Getters and Setters
	public Long getIdProfesional() {
		return idProfesional;
	}

	public void setIdProfesional(Long idProfesional) {
		this.idProfesional = idProfesional;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public boolean getEstadoActivoProfesional() {
		return estadoActivoProfesional;
	}

	public void setEstadoActivoProfesional(boolean estadoActivoProfesional) {
		this.estadoActivoProfesional = estadoActivoProfesional;
	}
}
