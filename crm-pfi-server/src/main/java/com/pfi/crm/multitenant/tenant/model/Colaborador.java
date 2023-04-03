package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ColaboradorPayload;

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
		this.setIdColaborador(null);
		this.setEstadoActivoColaborador(true);
	}
	
	
	public Colaborador(ColaboradorPayload payload) {
		super();
		this.modificar(payload);
		this.setIdColaborador(null);
		this.setEstadoActivoColaborador(true);
	}
	
	public void modificar(ColaboradorPayload payload) {
		this.modificarTrabajador(payload);
		this.setArea(payload.getArea());
	}
	
	public ColaboradorPayload toPayload() {
		ColaboradorPayload payload = new ColaboradorPayload();
		payload.modificarTrabajadorPayload(this.toTrabajadorAbstractPayload());
		payload.setArea(this.getArea());
		return payload;
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
}
