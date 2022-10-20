package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;

@Entity
@Table(name ="actividad")
public class Actividad extends UserDateAudit {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean estadoActivoActividad;
	private LocalDate fechaAltaActividad;
	private LocalDate fechaBajaActividad;
	
	private LocalDate fechaHoraDesde;	
	private LocalDate fechaHoraHasta;
	
	private String descripcion;
	
	@OneToMany()//fetch = FetchType.EAGER) //Fue reemplazado el fetch por lazyCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idBeneficiario ASC")
	private List<Beneficiario> beneficiarios;
	
	@OneToMany()//fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idProfesional ASC")
	private List<Profesional> profesionales;
	
	public Actividad() {
		super();
		fechaAltaActividad = LocalDate.now();
		beneficiarios = new ArrayList<Beneficiario>();
		profesionales = new ArrayList<Profesional>();
	}
	
	public Actividad(ActividadPayload p) {
		super();
		this.id = p.getId();
		this.fechaAltaActividad = null;
		this.fechaBajaActividad = null;
		this.fechaHoraDesde = p.getFechaHoraDesde();
		this.fechaHoraHasta = p.getFechaHoraHasta();
		this.descripcion = p.getDescripcion();
		p.getBeneficiarios().forEach((b) -> beneficiarios.add(new Beneficiario(b)));
		p.getProfesionales().forEach((pr) -> profesionales.add(new Profesional(pr)));
	}
	
	
	public ActividadPayload toPayload() {
		ActividadPayload p = new ActividadPayload();
		p.setId(id);
		p.setFechaHoraDesde(fechaHoraDesde);
		p.setFechaHoraHasta(fechaHoraHasta);
		beneficiarios.forEach((m) -> p.agregarBeneficiario(m.toPayload()));
		profesionales.forEach((m) -> p.agregarProfesional(m.toPayload()));
		return p;
	}
	
	
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEstadoActivoActividad() {
		return estadoActivoActividad;
	}

	public void setEstadoActivoActividad(boolean estadoActivoActividad) {
		this.estadoActivoActividad = estadoActivoActividad;
	}

	public LocalDate getFechaAltaActividad() {
		return fechaAltaActividad;
	}

	public void setFechaAltaActividad(LocalDate fechaAltaActividad) {
		this.fechaAltaActividad = fechaAltaActividad;
	}

	public LocalDate getFechaBajaActividad() {
		return fechaBajaActividad;
	}

	public void setFechaBajaActividad(LocalDate fechaBajaActividad) {
		this.fechaBajaActividad = fechaBajaActividad;
	}

	public LocalDate getFechaHoraDesde() {
		return fechaHoraDesde;
	}

	public void setFechaHoraDesde(LocalDate fechaHoraDesde) {
		this.fechaHoraDesde = fechaHoraDesde;
	}

	public LocalDate getFechaHoraHasta() {
		return fechaHoraHasta;
	}

	public void setFechaHoraHasta(LocalDate fechaHoraHasta) {
		this.fechaHoraHasta = fechaHoraHasta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(List<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	public List<Profesional> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(List<Profesional> profesionales) {
		this.profesionales = profesionales;
	}
}
