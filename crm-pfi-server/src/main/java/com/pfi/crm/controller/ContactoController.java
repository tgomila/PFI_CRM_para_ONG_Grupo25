package com.pfi.crm.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.model.Contacto;
import com.pfi.crm.payload.ContactoPayload;
import com.pfi.crm.service.ContactoService;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {
	
	@Autowired
	private ContactoService contactoService;
	
	
	
	@GetMapping("/{id}")
    public ContactoPayload getContactoById(@PathVariable Long id) {
        return contactoService.getContactoById(id).toPayload();
    }
	
	@GetMapping("/all")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<ContactoPayload> getContactos() {
    	return  contactoService.getContactos().stream().map(e -> contactoService.toPayload(e)).collect(Collectors.toList());
	}
	
	@PostMapping({"/", "/alta"})
    public ContactoPayload altaContacto(@Valid @RequestBody ContactoPayload payload) {
    	return contactoService.altaContacto(new Contacto(payload)).toPayload();
    }
	
	@PostMapping({"/baja/{id}"})
    public void bajaContacto(@PathVariable Long id) {
    	contactoService.bajaContacto(id);
    }	
	
	@PostMapping("/modificar")
    public ContactoPayload modificarContacto(@Valid @RequestBody ContactoPayload payload) {
    	return contactoService.modificarContacto(new Contacto(payload)).toPayload();
    }
	
	// TEST
	// Devuelve un ejemplo de contacto payload

	@GetMapping("/test")
	public ContactoPayload altaConsejoAdHonoremTest(/* @Valid @RequestBody ContactoPayload payload */) {

		Contacto m = new Contacto();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Contacto Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		return m.toPayload();
	}
}
