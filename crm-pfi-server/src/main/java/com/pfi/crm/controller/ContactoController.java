package com.pfi.crm.controller;

import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ContactoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ContactoService;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {
	
	@Autowired
	private ContactoService contactoService;
	
	
	
	@GetMapping("/{id}")
    public ContactoPayload getContactoById(@PathVariable Long id) {
        return contactoService.getContactoById(id).toPayload();
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<ContactoPayload> getContactos() {
		System.out.println("\n\n\n----------------------------Entre acá /all-----------------------------------\n\n\n");
    	//return  contactoService.getContactos().stream().map(e -> contactoService.toPayload(e)).collect(Collectors.toList());
		return  contactoService.getContactos();
	}
	
	@PostMapping({"/", "/alta"})
    public ContactoPayload altaContacto(@Valid @RequestBody ContactoPayload payload) {
    	return contactoService.altaContacto(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaContacto(@PathVariable Long id) {
    	contactoService.bajaContacto(id);
    }	
	
	@PutMapping({"/", "/modificar"})
    public ContactoPayload modificarContacto(@Valid @RequestBody ContactoPayload payload) {
    	return contactoService.modificarContacto(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return  new ContactoNombreTablaPayload().getNombresContactoTabla();
	}
	
	// TEST
	// Devuelve un ejemplo de contacto payload

	@GetMapping("/test")
	public ContactoPayload getContactoTest(/* @Valid @RequestBody ContactoPayload payload */) {

		ContactoPayload p = new ContactoPayload();

		// Contacto
		//m.setEstadoActivoContacto(true);
		p.setNombreDescripcion("Contacto Don Roque");
		p.setCuit("20-1235678-9");
		p.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		p.setEmail("felipe@gmail.com");
		p.setTelefono("1234-4567");

		return p;
	}
}
