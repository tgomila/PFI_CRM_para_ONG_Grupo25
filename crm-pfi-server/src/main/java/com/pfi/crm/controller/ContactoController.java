package com.pfi.crm.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ContactoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ContactoService;
import com.pfi.crm.payload.response.ApiResponse;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {
	
	@Autowired
	private ContactoService contactoService;
	
	
	
	@GetMapping("/{id}")
    public ContactoPayload getContactoById(@PathVariable Long id) {
        return contactoService.getContactoById(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ContactoPayload> getContactos() {
		System.out.println("\n\n\n----------------------------Entre acá /all-----------------------------------\n\n\n");
    	//return  contactoService.getContactos().stream().map(e -> contactoService.toPayload(e)).collect(Collectors.toList());
		return  contactoService.getContactos();
	}
	
	@PostMapping({"/", "/alta"})
    public ContactoPayload altaContacto(@Valid @RequestBody ContactoPayload payload) {
		System.out.println("\n\nEntre acáaaaaaaaaaaaaaaaaa\n\n");
    	return contactoService.altaContacto(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public ResponseEntity<?> bajaContacto(@PathVariable Long id) {
    	String message = contactoService.bajaContacto(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
	
	@GetMapping("/test/fecha")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Instant getFecha(/* @Valid @RequestBody ContactoPayload payload */) {
		Instant now = Instant.now();
		Instant firstDayOfMonth = now.atZone(ZoneId.systemDefault())
			    .toLocalDate()
			    .withDayOfMonth(1)
			    .atStartOfDay()
			    .atZone(ZoneId.systemDefault())
			    .toInstant();
		
		return firstDayOfMonth;
	}
	
	@GetMapping("/test/contactos_este_mes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public int getContactosEsteMes(/* @Valid @RequestBody ContactoPayload payload */) {
		return contactoService.getContactosCreadosEsteMes().size();
	}
	
	@GetMapping("/test/contar_contactos/este_mes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEsteMes() {
		return contactoService.countContactosCreadosEsteAnioPorMes();
	}
	
	@GetMapping("/test/contar_contactos/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosUltimos12meses() {
		return contactoService.countContactosCreadosUltimos12meses();
	}
	
	@GetMapping("/test/contar_contactos/este_anio/")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEsteAnio() {
		int esteAnio = LocalDate.now().getYear();
		return contactoService.countContactosByAnioMes(esteAnio);
	}
	
	@GetMapping("/test/contar_contactos/este_anio/{anioInput}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countContactosCreadosEnAnioInput(@PathVariable int anioInput) {
		return contactoService.countContactosByAnioMes(anioInput);
	}
}
