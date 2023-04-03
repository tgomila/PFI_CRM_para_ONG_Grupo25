package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.VoluntarioPayload;

@Entity
@Table(name="voluntario")
public class Voluntario extends PersonaFisicaAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idVoluntario;
	private boolean estadoActivoVoluntario = true;
	
	//Constructores
	public Voluntario() {
		super();
		this.setIdVoluntario(null);
		this.setEstadoActivoVoluntario(true);
	}
	
	public Voluntario(VoluntarioPayload payload) {
		super();
		this.modificar(payload);
		this.setIdVoluntario(null);
		this.setEstadoActivoVoluntario(true);
	}
	
	public void modificar(VoluntarioPayload payload) {
		this.modificarPersonaFisica(payload);
	}
	
	public VoluntarioPayload toPayload() {
		VoluntarioPayload payload = new VoluntarioPayload();
		payload.modificarPersonaFisica(this.toPersonaFisicaPayload());
		return payload;
	}
	
	
	//Getters y Setters
	public Long getIdVoluntario() {
		return idVoluntario;
	}

	public void setIdVoluntario(Long idVoluntario) {
		this.idVoluntario = idVoluntario;
	}
	
	public boolean getEstadoActivoVoluntario() {
		return estadoActivoVoluntario;
	}

	public void setEstadoActivoVoluntario(boolean estadoActivoVoluntario) {
		this.estadoActivoVoluntario = estadoActivoVoluntario;
	}
}
