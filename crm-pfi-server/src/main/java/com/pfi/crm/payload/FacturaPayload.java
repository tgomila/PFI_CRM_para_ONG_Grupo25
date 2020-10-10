package com.pfi.crm.payload;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaPayload {
	
	private Long id;
	private LocalDate fecha;
	private ContactoPayload cliente;
	private ContactoPayload emisorFactura;
	private List<FacturaItemPayload> itemsFactura;
	
	public FacturaPayload() {
		super();
		cliente = new ContactoPayload();
		emisorFactura = new ContactoPayload();
		itemsFactura = new ArrayList<FacturaItemPayload>();
	}
	
	public void agregarItemFactura(FacturaItemPayload item) {
		itemsFactura.add(item);
	}
	
	
	
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
	public ContactoPayload getCliente() {
		return cliente;
	}
	public void setCliente(ContactoPayload cliente) {
		this.cliente = cliente;
	}
	public ContactoPayload getEmisorFactura() {
		return emisorFactura;
	}
	public void setEmisorFactura(ContactoPayload emisorFactura) {
		this.emisorFactura = emisorFactura;
	}
	public List<FacturaItemPayload> getItemsFactura() {
		return itemsFactura;
	}
	public void setItemsFactura(List<FacturaItemPayload> itemsFactura) {
		this.itemsFactura = itemsFactura;
	}
	
	
}
