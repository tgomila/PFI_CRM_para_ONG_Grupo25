package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;

@Entity
@Table(name = "modulo_visibilidad_por_rol_tipo")
public class ModuloVisibilidadPorRolTipo {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modulo_visibilidad_por_rol_tipo_seq")
	@SequenceGenerator(name = "modulo_visibilidad_por_rol_tipo_seq", sequenceName = "modulo_visibilidad_por_rol_tipo_sequence", allocationSize = 1)
	private Long id;
	
	//private String path;
	
	//private String name;
	
	//private String iconName;
	
	@Enumerated(EnumType.STRING)
	//@NaturalId
	//@Column(length = 60)
	private ModuloEnum moduloEnum;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 60)
	private ModuloTipoVisibilidadEnum tipoVisibilidad;

	public ModuloVisibilidadPorRolTipo() {
		super();
	}

	public ModuloVisibilidadPorRolTipo(@NotNull ModuloEnum moduloEnum, @NotNull ModuloTipoVisibilidadEnum tipoVisibilidad) {
		super();
		this.id = null;
		
		this.moduloEnum = moduloEnum;
		this.tipoVisibilidad = tipoVisibilidad;
		chequearVisibilidadRespectoSiEsFreeOPaid();
	}

	public ModuloVisibilidadPorRolTipo(Long id, @NotNull ModuloEnum moduloEnum, @NotNull ModuloTipoVisibilidadEnum tipoVisibilidad) {
		super();
		this.id = id;
		this.moduloEnum = moduloEnum;
		this.tipoVisibilidad = tipoVisibilidad;
		chequearVisibilidadRespectoSiEsFreeOPaid();
	}
	
	public ModuloItemPayload toPayload(){
		ModuloItemPayload itemPayload = new ModuloItemPayload();
		itemPayload.setOrder(moduloEnum.getOrder());
		itemPayload.setModuloEnum(moduloEnum.toString());
		itemPayload.setName(moduloEnum.getName());
		itemPayload.setPath(moduloEnum.getPath());
		itemPayload.setIconName(moduloEnum.getIconName());
		itemPayload.setTipoVisibilidad(tipoVisibilidad.toString());
		itemPayload.setPriceOneMonth(moduloEnum.getPriceOneMonth());
		itemPayload.setPriceOneYear(moduloEnum.getPriceOneYear());
		return itemPayload;
	}
	
	/**
	 * 
	 * @return true si se modificó y deba guardarse, false si no se modifico.
	 */
	public boolean chequearVisibilidadRespectoSiEsFreeOPaid() {
		//if(moduloEnum == null || tipoVisibilidad == null)
		//	return;
		if(moduloEnum.isFreeModule() && tipoVisibilidad.equals(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION)) {
			tipoVisibilidad = ModuloTipoVisibilidadEnum.NO_VISTA;
			return true;
		}
		else {
			return false;
		}
		//Si es paid y es sin suscripción, ok.
		//Si es paid y es no vista, ok, porque no sabemos su rol o si esta suscripto.
	}
	
	public void suscribir() {
		if(moduloEnum.isPaidModule() && tipoVisibilidad.equals(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION))
			tipoVisibilidad = ModuloTipoVisibilidadEnum.NO_VISTA;
	}
	
	public void desuscribir() {
		if(moduloEnum.isPaidModule())
			tipoVisibilidad = ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION;
	}
	
	public boolean poseePermiso(ModuloTipoVisibilidadEnum permisoRequerido) {
		return tipoVisibilidad.poseePermiso(permisoRequerido);
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
	
	/**
	 * 
	 * @param moduloEnum
	 * @return true si se modificaron datos, false si no se ha modificado nada.
	 */
	public boolean setModuloEnum(ModuloEnum moduloEnum) {
		boolean seModificoEnum = false;
		if(!this.moduloEnum.equals(moduloEnum))
			seModificoEnum = true;
		this.moduloEnum = moduloEnum;
		boolean seModificoVisibilidad = chequearVisibilidadRespectoSiEsFreeOPaid();
		return (seModificoEnum || seModificoVisibilidad); 
	}

	public ModuloTipoVisibilidadEnum getTipoVisibilidad() {
		return tipoVisibilidad;
	}
	
	/**
	 * 
	 * @param tipoVisibilidad
	 * @return true si se modificaron datos, false si no se ha modificado nada.
	 */
	public boolean setTipoVisibilidad(ModuloTipoVisibilidadEnum tipoVisibilidad) {
		//Puede ser input SinSuscripcion, y cambiar a NoVista porque es free.
		ModuloTipoVisibilidadEnum oldVisibilidad = this.tipoVisibilidad;
		this.tipoVisibilidad = tipoVisibilidad;
		chequearVisibilidadRespectoSiEsFreeOPaid();
		if(oldVisibilidad == null && tipoVisibilidad != null)
			return true;	//No tenia visibilidad, y ahora ya tiene instancia.
		if(oldVisibilidad.equals(this.tipoVisibilidad))
			return false;	//Si no se modificó (son iguales), devuelvo false.
		else
			return true;	//Si se modificó (no son iguales), devuelvo true.
	}
	
	public int getOrder() {
		return moduloEnum.getOrder();
	}
	
	public double getPriceOneMonth() {
		return moduloEnum.getPriceOneMonth();
	}
	
	public double getPriceOneYear() {
		return moduloEnum.getPriceOneYear();
	}
	
}
