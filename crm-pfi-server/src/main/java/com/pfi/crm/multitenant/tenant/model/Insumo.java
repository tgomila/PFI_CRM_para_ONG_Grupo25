package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.InsumoPayload;

@Entity
@Table(name ="insumo")
public class Insumo extends UserDateAudit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2953441460328381036L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insumo_seq")
	//@SequenceGenerator(name = "insumo_seq", sequenceName = "insumo_sequence", allocationSize = 1)
	private Long id;
	private String tipo;
	private String descripcion;
	private int stockActual;
	private boolean estadoActivo;
	private boolean fragil;
	
	
	public Insumo() {
		super();
		this.estadoActivo = true;
	}
	
	public Insumo(InsumoPayload p) {
		super();
		this.id = p.getId();
		this.tipo = p.getTipo();
		this.descripcion = p.getDescripcion();
		this.stockActual = p.getStockActual();
		this.estadoActivo = true;
		this.fragil = p.isFragil();
	}
	
	public void modificar(InsumoPayload p) {
		this.tipo = p.getTipo();
		this.descripcion = p.getDescripcion();
		this.stockActual = p.getStockActual();
		this.fragil = p.isFragil();
	}
	
	public InsumoPayload toPayload() {
		InsumoPayload p = new InsumoPayload();
		p.setId(id);
		p.setTipo(tipo);
		p.setDescripcion(descripcion);
		p.setStockActual(stockActual);
		p.setFragil(fragil);
		return p;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getStockActual() {
		return stockActual;
	}

	public void setStockActual(int stockActual) {
		this.stockActual = stockActual;
	}

	public boolean isEstadoActivo() {
		return estadoActivo;
	}

	public void setEstadoActivo(boolean estadoActivo) {
		this.estadoActivo = estadoActivo;
	}

	public boolean isFragil() {
		return fragil;
	}

	public void setFragil(boolean fragil) {
		this.fragil = fragil;
	}
	
}
