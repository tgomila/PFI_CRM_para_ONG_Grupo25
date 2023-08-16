package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ProgramaDeActividadesPayload;

@Entity
@Table(name ="programa")
public class ProgramaDeActividades extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8844739142353777444L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "programa_de_actividades_seq")
	@SequenceGenerator(name = "programa_de_actividades_seq", sequenceName = "programa_de_actividades_sequence", allocationSize = 1)
	private Long id;
	
	private Boolean estadoActivoPrograma;
	private LocalDateTime fechaAltaPrograma;
	private LocalDateTime fechaBajaPrograma;
	
	private String descripcion;
	
	/*
	@OneToMany(
			fetch = FetchType.EAGER, 
			cascade = {CascadeType.MERGE}, 
			orphanRemoval = true
			)
	@OrderBy("fechaHoraDesde ASC")
	*/
	@OneToMany(cascade = CascadeType.DETACH)//fetch = FetchType.EAGER) //Fue reemplazado el fetch por lazyCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("fechaHoraDesde ASC")
	private List<Actividad> actividades;
	
	//Constructores
	public ProgramaDeActividades() {
		super();
		estadoActivoPrograma = true;
		fechaAltaPrograma = LocalDateTime.now();
		actividades = new ArrayList<Actividad>();
	}
	
	public ProgramaDeActividades(ProgramaDeActividadesPayload p, List<Actividad> actividades) {
		super();
		this.id = p.getId();
		estadoActivoPrograma = true;
		fechaAltaPrograma = LocalDateTime.now();
		//p.getActividades().forEach((a) -> actividades.add(new Actividad(a)));
		modificar(p, actividades);
	}
	
	public void modificar(ProgramaDeActividadesPayload p, List<Actividad> actividades) {
		this.descripcion = p.getDescripcion();
		this.actividades = actividades;
		//p.getActividades().forEach((a) -> actividades.add(new Actividad(a)));
		ordenarActividades();
	}
	
	
	public ProgramaDeActividadesPayload toPayload() {
		ordenarActividades();
		ProgramaDeActividadesPayload p = new ProgramaDeActividadesPayload();
		p.setId(id);
		p.setFechaDesde(this.getFechaInicio());
		p.setFechaHasta(this.getFechaFin());
		p.setDescripcion(this.descripcion);
		actividades.forEach((m) -> p.agregarActividad(m.toPayload()));
		return p;
	}
	
	public LocalDateTime getFechaInicio() {
		ordenarActividades();
		if(actividades.isEmpty())
			throw new BadRequestException("No hay actividades dentro del programa");
		else
			return actividades.get(0).getFechaHoraDesde();
		

		//List<Actividad> copy = new ArrayList<>(actividades);
		//return copy.stream()
		//		.filter(actividad -> actividad!=null && actividad.getFechaHoraDesde()!=null)
		//		.map(Actividad::getFechaHoraDesde)
		//		.min(LocalDateTime::compareTo)
		//		//.orElse(null);
		//		.get();
	}
	
	public LocalDateTime getFechaFin() {
		List<Actividad> copy = new ArrayList<>(actividades);
		return copy.stream()
				.filter(Objects::nonNull)
				//.filter(actividad -> {return actividad!=null && actividad.getFechaHoraHasta()!=null;})
				.filter(actividad -> actividad.getFechaHoraHasta()!=null)
				.map(Actividad::getFechaHoraHasta)
				.max(LocalDateTime::compareTo)
				.get();
	}
	
	public void agregarActividad(Actividad actividad) {
		actividades.add(actividad);
		ordenarActividades();
	}
	
	public void ordenarActividades() {
		this.actividades = ordenarActividades(this.actividades);
	}
	
	public List<Actividad> ordenarActividades(List<Actividad> actividadesSinOrdenar) {
		if(actividadesSinOrdenar == null)
			actividadesSinOrdenar = new ArrayList<Actividad>();
		if(actividadesSinOrdenar.isEmpty())
			return actividadesSinOrdenar;
		
		List<Actividad> actividadesOrdenadas = actividadesSinOrdenar
                .stream()
                .sorted(Comparator.comparing(Actividad::getFechaHoraDesde))
                .collect(Collectors.toList());
		return actividadesOrdenadas;
	}
	
	public void ordenarActividades2() {
		Collections.sort(actividades, new Actividad.LocalDateTimeComparator());
	}
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public Boolean getEstadoActivoPrograma() {
		return estadoActivoPrograma;
	}

	public void setEstadoActivoPrograma(Boolean estadoActivoPrograma) {
		this.estadoActivoPrograma = estadoActivoPrograma;
	}

	public void setEstadoActivoPrograma(boolean estadoActivoPrograma) {
		if(!estadoActivoPrograma)
			this.fechaBajaPrograma = LocalDateTime.now();
		else
			this.fechaBajaPrograma = null;
		this.estadoActivoPrograma = estadoActivoPrograma;
	}

	public LocalDateTime getFechaAltaPrograma() {
		return fechaAltaPrograma;
	}

	public LocalDateTime getFechaBajaPrograma() {
		return fechaBajaPrograma;
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
