package com.pfi.crm.multitenant.tenant.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;

@Entity
@Table(name ="producto")
public class Producto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private BigDecimal precioVenta;
	private int cantFijaCompra;
	private int cantMinimaStock;
	private int stockActual;
	private boolean estadoActivo;
	private boolean fragil;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private Contacto proveedor;
	
	//Constructores
	public Producto() {
		super();
	}
	
	
	public Producto(ProductoPayload p) {
		super();
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.precioVenta = p.getPrecioVenta();
		this.cantFijaCompra = p.getCantFijaCompra();
		this.cantMinimaStock = p.getCantMinimaStock();
		this.stockActual = p.getStockActual();
		this.estadoActivo = p.isEstadoActivo();
		this.fragil = p.isFragil();
	}
	
	public ProductoPayload toPayload() {
		ProductoPayload p = new ProductoPayload();
		
		p.setId(id);
		p.setDescripcion(descripcion);
		p.setPrecioVenta(precioVenta);
		p.setCantFijaCompra(cantFijaCompra);
		p.setCantMinimaStock(cantMinimaStock);
		p.setStockActual(stockActual);
		p.setEstadoActivo(estadoActivo);
		p.setFragil(fragil);
		
		return p;
	}

	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getCantFijaCompra() {
		return cantFijaCompra;
	}

	public void setCantFijaCompra(int cantFijaCompra) {
		this.cantFijaCompra = cantFijaCompra;
	}

	public int getCantMinimaStock() {
		return cantMinimaStock;
	}

	public void setCantMinimaStock(int cantMinimaStock) {
		this.cantMinimaStock = cantMinimaStock;
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
	
	public Contacto getProveedor() {
		return proveedor;
	}

	public void setProveedor(Contacto proveedor) {
		this.proveedor = proveedor;
	}
	
}