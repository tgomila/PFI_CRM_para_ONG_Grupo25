package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;

@Entity
@Table(name ="prestamo")
public class Prestamo extends UserDateAudit {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private int cantidad;
	private LocalDate fechaPrestamoInicio;
	private LocalDate fechaPrestamoFin;
	private Contacto prestamista; //quien tiene el producto
	private Contacto prestatario; //quien tiene el producto
	
	public Prestamo() {
		super();
		cantidad = 0;
		fechaPrestamoInicio = LocalDate.now();
	}
	
	public Prestamo(PrestamoPayload p) {
		super();
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.cantidad = p.getCantidad();
		this.fechaPrestamoInicio = p.getFechaPrestamoInicio();
		this.fechaPrestamoFin = p.getFechaPrestamoFin();
		this.prestamista = new Contacto(p.getPrestamista());
		this.prestatario = new Contacto(p.getPrestatario());
	}
	
	public void modificar(PrestamoPayload p, Contacto prestamista, Contacto prestatario) {
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.cantidad = p.getCantidad();
		this.fechaPrestamoInicio = p.getFechaPrestamoInicio();
		this.fechaPrestamoFin = p.getFechaPrestamoFin();
		this.prestamista = prestamista;
		this.prestatario = prestatario;
	}
	
	
	public PrestamoPayload toPayload() {
		PrestamoPayload p = new PrestamoPayload();
		p.setId(id);
		p.setDescripcion(descripcion);
		p.setCantidad(cantidad);
		p.setFechaPrestamoInicio(fechaPrestamoInicio);
		p.setFechaPrestamoFin(fechaPrestamoFin);
		if(prestamista != null)
			p.setPrestamista(prestamista.toPayload());
		if(prestatario != null)
			p.setPrestatario(prestatario.toPayload());
		return p;
	}
	
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDate getFechaPrestamoInicio() {
		return fechaPrestamoInicio;
	}

	public void setFechaPrestamoInicio(LocalDate fechaPrestamoInicio) {
		this.fechaPrestamoInicio = fechaPrestamoInicio;
	}

	public LocalDate getFechaPrestamoFin() {
		return fechaPrestamoFin;
	}

	public void setFechaPrestamoFin(LocalDate fechaPrestamoFin) {
		this.fechaPrestamoFin = fechaPrestamoFin;
	}

	public Contacto getPrestamista() {
		return prestamista;
	}

	public void setPrestamista(Contacto prestamista) {
		this.prestamista = prestamista;
	}

	public Contacto getPrestatario() {
		return prestatario;
	}

	public void setPrestatario(Contacto prestatario) {
		this.prestatario = prestatario;
	}
}
