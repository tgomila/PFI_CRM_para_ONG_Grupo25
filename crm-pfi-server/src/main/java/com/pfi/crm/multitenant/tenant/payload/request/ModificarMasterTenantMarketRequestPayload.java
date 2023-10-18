package com.pfi.crm.multitenant.tenant.payload.request;

import java.time.LocalDateTime;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;

public class ModificarMasterTenantMarketRequestPayload {
	
	//private List<TenantPayload> tenants;//vacio o null es todos
	private Integer tenantId;//vacio o null es todos
	
	private String modulo;//vacío o null es todos
	
	private Boolean quitarPruebaUtilizadaATenant;//null/false es hacer nada, true es setear que nunca usó la prueba
	
	private LocalDateTime fechaMaximaSuscripcion;//null es hacer nada, si existe, minimo 2 días despues de hoy porque si son 23:59 y da de alta a las 00hs no te permite.
	
	private Integer numeroTiempoASumar;//null es hacer nada, ejemplo "1" "2"
	
	private String diaMesAnio;//null es hacer nada, "dia" "mes" "año" es sumar ese tiempo
	
	//puede ser "1" "día" o algo asi
	
	//private boolean suscripcionActiva;//si o si actualizar su suscripción
	
	//Getters and Setters

	public boolean isAllModulos() {
		return modulo == null;
	}

	public boolean isAllTenants() {
		return tenantId == null;
	}

	public ModuloEnum getModuloEnum() {
		if(modulo != null)
			return ModuloEnum.valueOf(modulo);
		return null;
	}

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public Boolean getQuitarPruebaUtilizadaATenant() {
		return quitarPruebaUtilizadaATenant;
	}

	public void setQuitarPruebaUtilizadaATenant(Boolean quitarPruebaUtilizadaATenant) {
		this.quitarPruebaUtilizadaATenant = quitarPruebaUtilizadaATenant;
	}

	public LocalDateTime getFechaMaximaSuscripcion() {
		return fechaMaximaSuscripcion;
	}

	public void setFechaMaximaSuscripcion(LocalDateTime fechaMaximaSuscripcion) {
		this.fechaMaximaSuscripcion = fechaMaximaSuscripcion;
	}

	public Integer getNumeroTiempoASumar() {
		return numeroTiempoASumar;
	}

	public void setNumeroTiempoASumar(Integer numeroTiempoASumar) {
		this.numeroTiempoASumar = numeroTiempoASumar;
	}

	public String getDiaMesAnio() {
		return diaMesAnio;
	}

	public void setDiaMesAnio(String diaMesAnio) {
		this.diaMesAnio = diaMesAnio;
	}
	
	
}
