package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	private LocalDateTime fecha;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@OrderBy("nombreDescripcion ASC")
	private Contacto cliente;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	private Contacto emisorFactura;
	
	@OneToMany(cascade = { CascadeType.REMOVE, CascadeType.MERGE }, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<FacturaItem> itemsFactura;
	
	
	
	
	public Factura(FacturaPayload p, Contacto cliente, Contacto emisorFactura) {
		super();
		this.id = p.getId();
		modificar(p, cliente, emisorFactura, null);
		itemsFactura = new ArrayList<FacturaItem>();
		p.getItemsFactura().forEach((item) -> itemsFactura.add(new FacturaItem(item)));
	}
	
	public void modificar(FacturaPayload p, Contacto cliente, Contacto emisorFactura, List<FacturaItem> itemsFactura) {
		this.fecha = p.getFecha();
		this.cliente = cliente;
		this.emisorFactura = emisorFactura;
		this.itemsFactura = itemsFactura;
	}




	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
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
		if(p.getCliente() != null)
			p.setCliente(cliente.toPayload());
		if(p.getEmisorFactura() != null)
			p.setEmisorFactura(emisorFactura.toPayload());
		itemsFactura.forEach((item) -> p.agregarItemFactura(item.toPayload()));
		
		return p;
	}
}
