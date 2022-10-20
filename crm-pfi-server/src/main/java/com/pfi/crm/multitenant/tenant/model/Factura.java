package com.pfi.crm.multitenant.tenant.model;

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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.pfi.crm.multitenant.tenant.model.audit.UserDateAudit;
import com.pfi.crm.multitenant.tenant.payload.FacturaItemPayload;
import com.pfi.crm.multitenant.tenant.payload.FacturaPayload;

@Entity
@Table(name ="factura")
public class Factura extends UserDateAudit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022182382691725995L;

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
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<FacturaItem> itemsFactura;
	
	public Factura() {
		super();
		cliente = new Contacto();
		itemsFactura = new ArrayList<FacturaItem>();
	}
	
	
	
	
	public Factura(FacturaPayload p) {
		super();
		this.id = p.getId();
		this.fecha = p.getFecha();
		this.cliente = new Contacto(p.getCliente());
		this.emisorFactura = new Contacto(p.getEmisorFactura());
		itemsFactura = new ArrayList<FacturaItem>();
		for(FacturaItemPayload item:  p.getItemsFactura()) {
			itemsFactura.add(new FacturaItem(item));
		}
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
	
	
	
	
	public FacturaPayload toPayload() {
		FacturaPayload p = new FacturaPayload();
		
		p.setId(id);
		p.setFecha(fecha);
		p.setCliente(cliente.toPayload());
		p.setEmisorFactura(emisorFactura.toPayload());
		itemsFactura = new ArrayList<FacturaItem>();
		for(FacturaItem item:  itemsFactura) {
			p.agregarItemFactura(item.toPayload());
		}
		
		return p;
	}
}
