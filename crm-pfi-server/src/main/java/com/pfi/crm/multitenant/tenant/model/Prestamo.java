package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;

@Entity
@Table(name ="prestamo")
public class Prestamo extends UserDateAudit {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prestamo_seq")
	//@SequenceGenerator(name = "prestamo_seq", sequenceName = "prestamo_sequence", allocationSize = 1)
	private Long id;
	
	private String descripcion;
	private int cantidad;
	private LocalDateTime fechaPrestamoInicio;
	private LocalDateTime fechaPrestamoFin;
	private boolean haSidoDevuelto;
	@ManyToOne(cascade = CascadeType.MERGE )
	private Contacto prestamista; //Quien que se dedica a prestar algo.
	@ManyToOne(cascade = CascadeType.MERGE )
	private Contacto prestatario; //Quien toma algo a préstamo.
	
	public Prestamo() {
		super();
		cantidad = 0;
		fechaPrestamoInicio = LocalDateTime.now();
		haSidoDevuelto = false;
	}
	
	public Prestamo(PrestamoPayload p) {
		super();
		this.id = p.getId();
		this.modificar(p);
	}
	
	public Prestamo(PrestamoPayload p, Contacto prestamista, Contacto prestatario) {
		super();
		this.id = p.getId();
		this.modificar(p, prestamista, prestatario);
	}
	
	/**
	 * No incluye prestamista o prestatario
	 * @param p
	 */
	public void modificar(PrestamoPayload p) {
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.cantidad = p.getCantidad();
		this.fechaPrestamoInicio = p.getFechaPrestamoInicio();
		this.fechaPrestamoFin = p.getFechaPrestamoFin();
		this.haSidoDevuelto = p.isHaSidoDevuelto();
		this.prestamista = ((p.getPrestamista() != null) ? new Contacto(p.getPrestamista()) : null);
		this.prestatario = ((p.getPrestatario() != null) ? new Contacto(p.getPrestatario()) : null);
	}
	
	public void modificar(PrestamoPayload p, Contacto prestamista, Contacto prestatario) {
		this.modificar(p);
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
		p.setHaSidoDevuelto(haSidoDevuelto);
		p.setPrestamista((this.prestamista != null) ? this.prestamista.toPayload() : null);
		p.setPrestatario((this.prestatario != null) ? this.prestatario.toPayload() : null);
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

	public LocalDateTime getFechaPrestamoInicio() {
		return fechaPrestamoInicio;
	}

	public void setFechaPrestamoInicio(LocalDateTime fechaPrestamoInicio) {
		this.fechaPrestamoInicio = fechaPrestamoInicio;
	}

	public LocalDateTime getFechaPrestamoFin() {
		return fechaPrestamoFin;
	}

	public void setFechaPrestamoFin(LocalDateTime fechaPrestamoFin) {
		this.fechaPrestamoFin = fechaPrestamoFin;
	}

	public boolean isHaSidoDevuelto() {
		return haSidoDevuelto;
	}

	public void setHaSidoDevuelto(boolean haSidoDevuelto) {
		this.haSidoDevuelto = haSidoDevuelto;
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
