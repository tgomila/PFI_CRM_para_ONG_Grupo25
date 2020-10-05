package com.pfi.crm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name ="factura")
public class Factura {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate fecha;
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private Contacto cliente;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Contacto emisorFactura;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	private List<FacturaItem> itemsFactura;
	
	public Factura() {
		super();
		cliente = new Contacto();
		itemsFactura = new ArrayList<FacturaItem>();
	}
	
	
	
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Contacto getCliente() {
		return cliente;
	}

	public void setCliente(Contacto cliente) {
		this.cliente = cliente;
	}

	public Contacto getEmisorFactura() {
		return emisorFactura;
	}

	public void setEmisorFactura(Contacto emisorFactura) {
		this.emisorFactura = emisorFactura;
	}

	public List<FacturaItem> getItemsFactura() {
		return itemsFactura;
	}

	public void setItemsFactura(List<FacturaItem> itemsFactura) {
		this.itemsFactura = itemsFactura;
	}
	
	
}