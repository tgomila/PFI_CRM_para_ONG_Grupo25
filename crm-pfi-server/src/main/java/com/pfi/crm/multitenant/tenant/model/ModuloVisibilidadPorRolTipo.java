package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;

@Entity
@Table(name = "modulo_visibilidad_por_rol_tipo")
public class ModuloVisibilidadPorRolTipo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//private String path;
	
	//private String name;
	
	//private String iconName;
	
	@Enumerated(EnumType.STRING)
	@NaturalId
	//@Column(length = 60)
	private ModuloEnum moduloEnum;
	
	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private ModuloTipoVisibilidadEnum tipoVisibilidad;

	public ModuloVisibilidadPorRolTipo() {
		super();
	}

	public ModuloVisibilidadPorRolTipo(ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		super();
		this.id = null;
		this.moduloEnum = moduloEnum;
		this.tipoVisibilidad = tipoVisibilidad;
	}

	public ModuloVisibilidadPorRolTipo(Long id, ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		super();
		this.id = id;
		this.moduloEnum = moduloEnum;
		this.tipoVisibilidad = tipoVisibilidad;
	}
	
	public ModuloItemPayload toPayload(){
		ModuloItemPayload itemPayload = new ModuloItemPayload();
		itemPayload.setName(moduloEnum.getName());
		itemPayload.setPath(moduloEnum.getPath());
		itemPayload.setIconName(moduloEnum.getIconName());
		itemPayload.setTipoVisibilidad(tipoVisibilidad.toString());
		return itemPayload;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModuloEnum getModuloEnum() {
		return moduloEnum;
	}

	public void setModuloEnum(ModuloEnum moduloEnum) {
		this.moduloEnum = moduloEnum;
	}

	public ModuloTipoVisibilidadEnum getTipoVisibilidad() {
		return tipoVisibilidad;
	}

	public void setTipoVisibilidad(ModuloTipoVisibilidadEnum tipoVisibilidad) {
		this.tipoVisibilidad = tipoVisibilidad;
	}
	
}
