package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.DonacionPayload;

@Entity
@Table(name="donacion")
public class Donacion extends UserDateAudit{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4353814294309655078L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime fecha;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@OrderBy("nombreDescripcion ASC")
	private Contacto donante;
	
	@Enumerated(EnumType.STRING)
	private DonacionTipo tipoDonacion;
	
	private String descripcion;
	
	
	public Donacion() {
		super();
		fecha = LocalDateTime.now();
		donante = null;//new Contacto();
	}
	
	/**
	 * No incluye setear donante
	 * @param p
	 */
	public Donacion(DonacionPayload p) {
		super();
		this.id = p.getId();
		modificar(p);
	}
	
	public void modificar(DonacionPayload p) {
		this.fecha = p.getFecha();
		this.donante = ((p.getDonante() != null) ? new Contacto(p.getDonante()) : null); 
		this.tipoDonacion = p.getTipoDonacion();
		this.descripcion = p.getDescripcion();
	}





	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Contacto getDonante() {
		return donante;
	}

	public void setDonante(Contacto donante) {
		this.donante = donante;
	}
	
	public DonacionTipo getTipoDonacion() {
		return tipoDonacion;
	}

	public void setTipoDonacion(DonacionTipo tipoDonacion) {
		this.tipoDonacion = tipoDonacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
	
	
	
	public DonacionPayload toPayload() {
		DonacionPayload p = new DonacionPayload();
		
		p.setId(id);
		p.setFecha(fecha);
		p.setDonante((this.donante != null) ? this.donante.toPayload() : null);
		p.setTipoDonacion(tipoDonacion);
		p.setDescripcion(descripcion);
		
		return p;
	}
}
