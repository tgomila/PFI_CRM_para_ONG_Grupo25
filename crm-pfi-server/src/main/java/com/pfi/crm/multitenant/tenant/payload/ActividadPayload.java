package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActividadPayload {
	
	private Long id;
	private LocalDate fechaHoraDesde;	
	private LocalDate fechaHoraHasta;
	private List<BeneficiarioPayload> beneficiarios;
	private List<ProfesionalPayload> profesionales;
	private String descripcion;
	
	public ActividadPayload() {
		super();
		beneficiarios = new ArrayList<BeneficiarioPayload>();
		profesionales = new ArrayList<ProfesionalPayload>();
	}
	
	public void agregarBeneficiario(BeneficiarioPayload beneficiario) {
		beneficiarios.add(beneficiario);
	}
	
	public void agregarProfesional(ProfesionalPayload profesional) {
		profesionales.add(profesional);
	}
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<BeneficiarioPayload> getBeneficiarios() {
		return beneficiarios;
	}

	public void setBeneficiarios(List<BeneficiarioPayload> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}

	public List<ProfesionalPayload> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(List<ProfesionalPayload> profesionales) {
		this.profesionales = profesionales;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
