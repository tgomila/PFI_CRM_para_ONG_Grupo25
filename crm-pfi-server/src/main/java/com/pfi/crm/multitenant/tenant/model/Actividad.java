package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;

@Entity
@Table(name ="actividad")
public class Actividad extends UserDateAudit implements Comparable < Actividad > {

	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actividad_seq")
	@SequenceGenerator(name = "actividad_seq", sequenceName = "actividad_sequence", allocationSize = 1)
	private Long id;
	
	private boolean estadoActivoActividad;
	
	private LocalDateTime fechaHoraDesde;	
	private LocalDateTime fechaHoraHasta;
	
	private String descripcion;
	
	@ManyToMany(cascade = CascadeType.MERGE)//fetch = FetchType.EAGER) //Fue reemplazado el fetch por lazyCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idBeneficiario ASC")
	private Set<Beneficiario> beneficiarios;
	
	@ManyToMany(cascade = CascadeType.MERGE)//fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idProfesional ASC")
	private Set<Profesional> profesionales;
	
	public Actividad() {
		super();
		this.setEstadoActivoActividad(true);
		beneficiarios = new HashSet<Beneficiario>();
		profesionales = new HashSet<Profesional>();
	}
	
	public Actividad(ActividadPayload p, Set<Beneficiario> beneficiarios, Set<Profesional> profesionales) {
		super();
		this.id = p.getId();
		this.estadoActivoActividad = true;
		this.modificar(p, profesionales, beneficiarios);
	}
	
	/**
	 * 
	 * @param payload
	 * @param profesionales, sacados de la BD.
	 * @param beneficiarios, sacados de la BD.
	 */
	public void modificar(ActividadPayload payload, Set<Profesional> profesionales, Set<Beneficiario> beneficiarios) {
		if(payload.getFechaHoraDesde() == null)
			new ResourceNotFoundException("Actividad", "fechaDesde", payload.getFechaHoraDesde());
		if(payload.getFechaHoraHasta() == null)
			new ResourceNotFoundException("Actividad", "fechaHasta", payload.getFechaHoraHasta());
		if(payload.getDescripcion() == null)
			new ResourceNotFoundException("Actividad", "descripción", payload.getDescripcion());
		
		this.fechaHoraDesde = payload.getFechaHoraDesde();
		this.fechaHoraHasta = payload.getFechaHoraHasta();
		this.descripcion = payload.getDescripcion();

		this.beneficiarios = beneficiarios != null ? beneficiarios : new HashSet<Beneficiario>();
		this.profesionales = profesionales != null ? profesionales : new HashSet<Profesional>();
	}
	
	
	public ActividadPayload toPayload() {
		ActividadPayload p = new ActividadPayload();
		p.setId(id);
		p.setFechaHoraDesde(fechaHoraDesde);
		p.setFechaHoraHasta(fechaHoraHasta);
		p.setDescripcion(descripcion);
		beneficiarios.forEach((m) -> p.agregarBeneficiario(m.toPayload()));
		profesionales.forEach((m) -> p.agregarProfesional(m.toPayload()));
		return p;
	}
	
	
	//Comparator
	//@Override
	//public int compare(Actividad o1, Actividad o2) {
	//	return o1.getFechaHoraDesde().compareTo(o2.getFechaHoraDesde());
	//}
	
	@Override
	public int compareTo(Actividad o) {
		return this.fechaHoraDesde.compareTo(o.getFechaHoraDesde());
	}
	
	public static class LocalDateTimeComparator implements Comparator<Actividad> {
		@Override
		public int compare(Actividad o1, Actividad o2) {
			return o1.getFechaHoraDesde().compareTo(o2.getFechaHoraDesde());
		}
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

	public LocalDateTime getFechaHoraDesde() {
		return fechaHoraDesde;
	}

	public void setFechaHoraDesde(LocalDateTime fechaHoraDesde) {
		this.fechaHoraDesde = fechaHoraDesde;
	}

	public LocalDateTime getFechaHoraHasta() {
		return fechaHoraHasta;
	}

	public void setFechaHoraHasta(LocalDateTime fechaHoraHasta) {
		this.fechaHoraHasta = fechaHoraHasta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<Beneficiario> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(Set<Beneficiario> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	public Set<Profesional> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(Set<Profesional> profesionales) {
		this.profesionales = profesionales;
	}
}
