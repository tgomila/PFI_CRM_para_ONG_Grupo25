package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ProyectoPayload;

@Entity
@Table(name ="proyecto")
public class Proyecto extends UserDateAudit {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	private String descripcion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idPersonaFisica ASC")
	private List<PersonaFisica> involucrados;
	
	public Proyecto() {
		super();
		fechaInicio = LocalDate.now();
		involucrados = new ArrayList<PersonaFisica>();
	}
	
	public Proyecto(ProyectoPayload p) {
		super();
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.fechaInicio = p.getFechaInicio();
		this.fechaFin = p.getFechaFin();
		involucrados = new ArrayList<PersonaFisica>();
		p.getInvolucrados().forEach((i) -> involucrados.add(new PersonaFisica(i)));
	}
	
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

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<PersonaFisica> getInvolucrados() {
		return involucrados;
	}

	public void setInvolucrados(List<PersonaFisica> involucrados) {
		this.involucrados = involucrados;
	}

	public ProyectoPayload toPayload() {
		ProyectoPayload p = new ProyectoPayload();
		p.setId(id);
		p.setDescripcion(descripcion);
		p.setFechaInicio(fechaInicio);
		p.setFechaFin(fechaFin);
		involucrados.forEach((m) -> p.agregarInvolucrado(m.toPayload()));
		return p;
	}
}
