package com.pfi.crm.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	//Constructores
	public FacturaItem() {
		super();
	}
	
	
	public FacturaItem(Long id, String descripcion, int unidades, BigDecimal precioUnitario, BigDecimal precio) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.unidades = unidades;
		this.precioUnitario = precioUnitario;
		this.precio = precio;
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
	
}
