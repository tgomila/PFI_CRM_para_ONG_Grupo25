package com.pfi.crm.multitenant.tenant.payload;

public class InsumoPayload {
	
	private Long id;
	private String tipo;
	private String descripcion;
	private int stockActual;
	private boolean fragil;
	
	public InsumoPayload() {
		super();
		stockActual = 0;
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

	public boolean isFragil() {
		return fragil;
	}

	public void setFragil(boolean fragil) {
		this.fragil = fragil;
	}
}
