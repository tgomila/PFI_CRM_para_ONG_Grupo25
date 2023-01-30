package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.exception.ResourceNotFoundException;
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
	
	private LocalDateTime fechaHoraDesde;	
	private LocalDateTime fechaHoraHasta;
	
	private String descripcion;
	
	@OneToMany()//fetch = FetchType.EAGER) //Fue reemplazado el fetch por lazyCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idBeneficiario ASC")
	private List<Beneficiario> beneficiarios;
	
	@OneToMany()//fetch = FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("idProfesional ASC")
	private List<Profesional> profesionales;
	
	/*public Actividad() {
		super();
		this.setEstadoActivoActividad(true);
		beneficiarios = new ArrayList<Beneficiario>();
		profesionales = new ArrayList<Profesional>();
	}*/
	
	public Actividad(ActividadPayload p) {
		super();
		if(p.getFechaHoraDesde() == null)
			new ResourceNotFoundException("Actividad", "fechaDesde", p.getFechaHoraDesde());
		if(p.getFechaHoraHasta() == null)
			new ResourceNotFoundException("Actividad", "fechaHasta", p.getFechaHoraHasta());
		this.id = p.getId();
		this.estadoActivoActividad = true;
		this.fechaHoraDesde = p.getFechaHoraDesde();
		this.fechaHoraHasta = p.getFechaHoraHasta();
		this.descripcion = p.getDescripcion();
		beneficiarios = new ArrayList<Beneficiario>();
		profesionales = new ArrayList<Profesional>();
		p.getBeneficiarios().forEach((b) -> beneficiarios.add(new Beneficiario(b)));
		p.getProfesionales().forEach((pr) -> profesionales.add(new Profesional(pr)));
	}
	
	/**
	 * 
	 * @param payload
	 * @param profesionales, sacados de la BD.
	 * @param beneficiarios, sacados de la BD.
	 */
	public void modificar(ActividadPayload payload, List<Profesional> profesionales, List<Beneficiario> beneficiarios) {
		if(payload.getFechaHoraDesde() == null)
			new ResourceNotFoundException("Actividad", "fechaDesde", payload.getFechaHoraDesde());
		if(payload.getFechaHoraHasta() == null)
			new ResourceNotFoundException("Actividad", "fechaHasta", payload.getFechaHoraHasta());
		if(payload.getDescripcion() == null)
			new ResourceNotFoundException("Actividad", "descripción", payload.getDescripcion());
		
		this.fechaHoraDesde = payload.getFechaHoraDesde();
		this.fechaHoraHasta = payload.getFechaHoraHasta();
		this.descripcion = payload.getDescripcion();
		
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
