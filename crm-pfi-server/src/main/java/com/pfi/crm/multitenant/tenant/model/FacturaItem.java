package com.pfi.crm.multitenant.tenant.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.FacturaItemPayload;

@Entity
@Table(name ="factura_item")
public class FacturaItem extends UserDateAudit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 330786093890005795L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_item_seq")
	@SequenceGenerator(name = "factura_item_seq", sequenceName = "factura_item_sequence", allocationSize = 1)
	private Long id;
	private String descripcion;
	private int unidades;
	private BigDecimal precioUnitario;
	private BigDecimal precio;
	
	//@ManyToOne(fetch = FetchType.LAZY, optional = false)
	//private Factura factura;
	
	//Constructor
	public FacturaItem() {
		super();
	}
	
	public FacturaItem(FacturaItemPayload p) {
		super();
		this.id = p.getId();
		modificar(p);
	}
	
	public void modificar(FacturaItemPayload p) {
		this.descripcion = p.getDescripcion();
		this.unidades = p.getUnidades();
		this.precioUnitario = p.getPrecioUnitario();
		this.precio = p.getPrecio();
	}
	
	public boolean isEquals(FacturaItemPayload p) {
		boolean equal = true;
		if(!this.id.equals(p.getId())
				|| !this.descripcion.equals(p.getDescripcion())
				|| !(this.unidades == p.getUnidades())
				|| !this.precioUnitario.equals(p.getPrecioUnitario())
				|| !this.precio.equals(p.getPrecio())) {
			equal = false;
		}
		return equal;
			
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
