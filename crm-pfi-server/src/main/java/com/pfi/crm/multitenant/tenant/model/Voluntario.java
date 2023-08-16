package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.VoluntarioPayload;

@Entity
@Table(name="voluntario")
public class Voluntario extends PersonaFisicaAbstract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1155059339619966053L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voluntario_seq")
	@SequenceGenerator(name = "voluntario_seq", sequenceName = "voluntario_sequence", allocationSize = 1)
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
