package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pfi.crm.model.audit.UserDateAudit;
import com.pfi.crm.payload.DonacionPayload;

@Entity
@Table(name="donacion")
public class Donacion extends UserDateAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate fecha;
	
	@ManyToOne(cascade = CascadeType.ALL)
	//@OrderBy("nombreDescripcion ASC")
	private Contacto donante;
	
	@Enumerated(EnumType.STRING)
	private DonacionTipo tipoDonacion;
	
	private String descripcion;
	
	
	public Donacion() {
		super();
		fecha = LocalDate.now();
		donante = new Contacto();
	}

	public Donacion(DonacionPayload p) {
		super();
		this.id = p.getId();
		this.fecha = p.getFecha();
		this.donante = new Contacto(p.getDonante());
		this.tipoDonacion = p.getTipoDonacion();
		this.descripcion = p.getDescripcion();
	}





	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
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
		p.setDonante(donante.toPayload());
		p.setTipoDonacion(tipoDonacion);
		p.setDescripcion(descripcion);
		
		return p;
	}
	
	public void modificar (DonacionPayload p) {
		//this.id = p.getId();
		this.fecha = p.getFecha();
		this.donante = new Contacto(p.getDonante());
		this.tipoDonacion = p.getTipoDonacion();
		this.descripcion = p.getDescripcion();
	}
}
