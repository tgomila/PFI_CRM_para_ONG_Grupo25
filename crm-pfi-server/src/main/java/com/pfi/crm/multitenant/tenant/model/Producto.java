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

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;

@Entity
@Table(name ="producto")
public class Producto extends UserDateAudit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226006295987384493L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
	//@SequenceGenerator(name = "producto_seq", sequenceName = "producto_sequence", allocationSize = 1)
	private Long id;
	private String tipo;
	private String descripcion;
	private BigDecimal precioVenta;
	private int cantFijaCompra;
	private int cantMinimaStock;
	private int stockActual;
	private boolean estadoActivo;
	private boolean fragil;
	
	@ManyToOne(cascade = {CascadeType.MERGE} )
	@OrderBy("nombreDescripcion ASC")
	private Contacto proveedor;
	
	//Constructores
	public Producto() {
		super();
	}
	
	public Producto(ProductoPayload p) {
		super();
		this.id = p.getId();
		this.estadoActivo = true;
		this.modificar(p);
	}

	public void modificar(ProductoPayload p) {
		this.tipo = p.getTipo();
		this.descripcion = p.getDescripcion();
		this.precioVenta = p.getPrecioVenta();
		this.cantFijaCompra = p.getCantFijaCompra();
		this.cantMinimaStock = p.getCantMinimaStock();
		this.stockActual = p.getStockActual();
		this.fragil = p.isFragil();
		this.proveedor = ((p.getProveedor() != null) ? new Contacto(p.getProveedor()) : null);
	}
	
	public ProductoPayload toPayload() {
		ProductoPayload p = new ProductoPayload();
		
		p.setId(id);
		p.setTipo(tipo);
		p.setDescripcion(descripcion);
		p.setPrecioVenta(precioVenta);
		p.setCantFijaCompra(cantFijaCompra);
		p.setCantMinimaStock(cantMinimaStock);
		p.setStockActual(stockActual);
		p.setFragil(fragil);
		p.setProveedor((this.proveedor != null) ? proveedor.toPayload() : null);
		
		return p;
	}

	//Getters and Setters
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
