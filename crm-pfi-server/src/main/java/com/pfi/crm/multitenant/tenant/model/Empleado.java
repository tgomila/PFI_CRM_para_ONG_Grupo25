package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;

@Entity
@Table(name="empleado")
public class Empleado extends TrabajadorAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6566425112277670572L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
	@SequenceGenerator(name = "empleado_seq", sequenceName = "empleado_sequence", allocationSize = 1)
	private Long idEmpleado;
	
	private String funcion;
	private String descripcion;
	private boolean estadoActivoEmpleado;
	
	//Constructor
	public Empleado() {
		super();
		this.setIdEmpleado(null);
		this.setEstadoActivoEmpleado(true);
	}
	
	public Empleado(EmpleadoPayload payload) {
		super();
		this.modificar(payload);
		this.setIdEmpleado(null);
		this.setEstadoActivoEmpleado(true);
	}
	
	public void modificar(EmpleadoPayload payload) {
		this.modificarTrabajador(payload);
		this.setFuncion(payload.getFuncion());
		this.setDescripcion(payload.getDescripcion());
	}
	
	public EmpleadoPayload toPayload() {
		EmpleadoPayload payload = new EmpleadoPayload();
		payload.modificarTrabajadorPayload(this.toTrabajadorAbstractPayload());
		payload.setFuncion(this.getFuncion());
		payload.setDescripcion(this.getDescripcion());
		return payload;
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
}
