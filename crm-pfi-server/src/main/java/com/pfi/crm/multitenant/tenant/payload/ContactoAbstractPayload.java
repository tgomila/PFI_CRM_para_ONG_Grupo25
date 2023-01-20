package com.pfi.crm.multitenant.tenant.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public abstract class ContactoAbstractPayload {
	
	//Solo esta para "modificar persona"
	private Long id;
	
	//private boolean estadoActivoContacto;
	//private LocalDate fechaAltaContacto;
	//private LocalDate fechaBajaContacto;
	@NotBlank
    @Size(max = 140)
	private String nombreDescripcion;
	
	@Size(max = 25)
	private String cuit;
	
	@Size(max = 200)
	private String domicilio;
	
	@Size(max = 140)
	private String email;
	
	@Size(max = 30)
	private String telefono;
	
	public ContactoAbstractPayload modificarContacto(ContactoAbstractPayload ap) {
		// Contacto
		//this.setId(p.getId());
		//this.setEstadoActivoContacto(true);
		//this.setFechaAltaContacto(LocalDate.now());
		//this.setFechaBajaContacto(null);
		this.setNombreDescripcion(ap.getNombreDescripcion());
		this.setCuit(ap.getCuit());
		this.setDomicilio(ap.getDomicilio());
		this.setEmail(ap.getEmail());
		this.setTelefono(ap.getTelefono());
		// Fin Contacto
		
		return this;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombreDescripcion() {
		return nombreDescripcion;
	}
	public void setNombreDescripcion(String nombreDescripcion) {
		this.nombreDescripcion = nombreDescripcion;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
}
