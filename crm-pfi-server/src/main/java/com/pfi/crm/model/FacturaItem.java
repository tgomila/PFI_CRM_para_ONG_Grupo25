package com.pfi.crm.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pfi.crm.payload.FacturaItemPayload;
import com.pfi.crm.payload.FacturaPayload;

@Entity
@Table(name ="factura_item")
public class FacturaItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private int unidades;
	private BigDecimal precioUnitario;
	private BigDecimal precio;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Factura factura;
	
	//Constructores
	public FacturaItem() {
		super();
	}
	
	
	public FacturaItem(FacturaItemPayload p) {
		super();
		this.id = p.getId();
		this.descripcion = p.getDescripcion();
		this.unidades = p.getUnidades();
		this.precioUnitario = p.getPrecioUnitario();
		this.precio = p.getPrecio();
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


	public int getUnidades() {
		return unidades;
	}


	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}


	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}


	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}


	public BigDecimal getPrecio() {
		return precio;
	}


	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}


	public Factura getFactura() {
		return factura;
	}


	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	
	
	
	public FacturaItemPayload toPayload() {
		FacturaItemPayload p = new FacturaItemPayload();
		
		p.setId(id);
		p.setDescripcion(descripcion);
		p.setUnidades(unidades);
		p.setPrecioUnitario(precioUnitario);
		p.setPrecio(precio);
		
		return p;
	}
}
