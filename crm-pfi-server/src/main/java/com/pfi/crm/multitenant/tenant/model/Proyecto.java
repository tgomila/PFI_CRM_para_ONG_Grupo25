package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ProyectoPayload;

@Entity
@Table(name ="proyecto")
public class Proyecto extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 953565293845694276L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proyecto_seq")
	//@SequenceGenerator(name = "proyecto_seq", sequenceName = "proyecto_sequence", allocationSize = 1)
	private Long id;
	
	private String descripcion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	//@OneToMany(cascade = {CascadeType.MERGE}, orphanRemoval=false)
	@ManyToMany(cascade = CascadeType.MERGE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idPersonaFisica ASC")
	private Set<PersonaFisica> involucrados;
	
	public Proyecto() {
		super();
		fechaInicio = LocalDate.now();
		involucrados = new HashSet<PersonaFisica>();
	}
	
	public Proyecto(ProyectoPayload p, Set<PersonaFisica> involucrados) {
		super();
		this.id = p.getId();
		this.modificar(p, involucrados);
	}
	
	public void modificar(ProyectoPayload payload, Set<PersonaFisica> involucrados) {
		if(payload.getFechaInicio() == null)
			new ResourceNotFoundException("Proyecto", "fechaInicio", payload.getFechaInicio());
		if(payload.getFechaFin() == null)
			new ResourceNotFoundException("Proyecto", "fechaFin", payload.getFechaFin());
		if(payload.getDescripcion() == null)
			new ResourceNotFoundException("Proyecto", "descripción", payload.getDescripcion());
		if(payload.getInvolucrados() == null)
			new ResourceNotFoundException("Proyecto", "involucrados", payload.getInvolucrados());
		
		this.descripcion = payload.getDescripcion();
		this.fechaInicio = payload.getFechaInicio();
		this.fechaFin = payload.getFechaFin();
		
		this.involucrados = involucrados != null ? involucrados : new HashSet<PersonaFisica>();
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

	public Set<PersonaFisica> getInvolucrados() {
		return involucrados;
	}

	public void setInvolucrados(Set<PersonaFisica> involucrados) {
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
