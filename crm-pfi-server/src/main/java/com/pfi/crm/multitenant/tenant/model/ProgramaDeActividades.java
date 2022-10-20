package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ProgramaDeActividadesPayload;

@Entity
@Table(name ="factura")
public class ProgramaDeActividades extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8844739142353777444L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean estadoActivoPrograma;
	private LocalDate fechaAltaPrograma;
	private LocalDate fechaBajaPrograma;
	
	private String descripcion;
	
	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy("fechaHoraDesde ASC")
	private List<Actividad> actividades;
	
	public ProgramaDeActividades() {
		super();
		fechaAltaPrograma = LocalDate.now();
		actividades = new ArrayList<Actividad>();
	}
	
	public ProgramaDeActividades(ProgramaDeActividadesPayload p) {
		super();
		this.id = p.getId();
		this.fechaAltaPrograma = null;
		this.fechaBajaPrograma = null;
		this.descripcion = p.getDescripcion();
		p.getActividades().forEach((a) -> actividades.add(new Actividad(a)));
	}
	
	
	public ProgramaDeActividadesPayload toPayload() {
		ProgramaDeActividadesPayload p = new ProgramaDeActividadesPayload();
		p.setId(id);
		p.setFechaDesde(this.getFechaInicio());
		p.setFechaHasta(this.getFechaFin());
		p.setFechaAltaPrograma(fechaAltaPrograma);
		p.setFechaBajaPrograma(fechaBajaPrograma);
		actividades.forEach((m) -> p.agregarActividad(m.toPayload()));
		return p;
	}
	
	public LocalDate getFechaInicio() {
		List<Actividad> copy = new ArrayList<>(actividades);
		return copy.stream()
				.filter(actividad -> actividad!=null && actividad.getFechaHoraDesde()!=null)
				.map(Actividad::getFechaHoraDesde)
				.min(LocalDate::compareTo)
				.orElse(null);
				//.get();
	}
	
	public LocalDate getFechaFin() {
		List<Actividad> copy = new ArrayList<>(actividades);
		return copy.stream()
				.filter(actividad -> actividad!=null && actividad.getFechaHoraDesde()!=null)
				.map(Actividad::getFechaHoraDesde)
				.max(LocalDate::compareTo)
				.orElse(null);
				//.get();
	}
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEstadoActivoPrograma() {
		return estadoActivoPrograma;
	}

	public void setEstadoActivoPrograma(boolean estadoActivoPrograma) {
		this.estadoActivoPrograma = estadoActivoPrograma;
	}

	public LocalDate getFechaAltaPrograma() {
		return fechaAltaPrograma;
	}

	public void setFechaAltaPrograma(LocalDate fechaAltaPrograma) {
		this.fechaAltaPrograma = fechaAltaPrograma;
	}

	public LocalDate getFechaBajaPrograma() {
		return fechaBajaPrograma;
	}

	public void setFechaBajaPrograma(LocalDate fechaBajaPrograma) {
		this.fechaBajaPrograma = fechaBajaPrograma;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Actividad> getActividades() {
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}
	
}
