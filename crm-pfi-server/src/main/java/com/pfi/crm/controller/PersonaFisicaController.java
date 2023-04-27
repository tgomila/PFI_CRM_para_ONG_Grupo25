package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PersonaFisicaNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.PersonaFisicaService;



@RestController
@RequestMapping("/api/personafisica")
public class PersonaFisicaController {
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	
	
	@GetMapping("/{id}")
    public PersonaFisicaPayload getPersonaFisicaById(@PathVariable Long id) {
        return personaFisicaService.getPersonaFisicaByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<PersonaFisicaPayload> getPersonaFisica() {
    	return  personaFisicaService.getPersonasFisicas();
	}
	
	@PostMapping({"/", "/alta"})
    public PersonaFisicaPayload altaPersonaFisica(@Valid @RequestBody PersonaFisicaPayload payload) {
    	return personaFisicaService.altaPersonaFisica(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaPersonaFisica(@PathVariable Long id) {
		personaFisicaService.bajaPersonaFisica(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public PersonaFisicaPayload modificarPersonaFisica(@Valid @RequestBody PersonaFisicaPayload payload) {
    	return personaFisicaService.modificarPersonaFisica(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new PersonaFisicaNombreTablaPayload().getNombresPersonaFisicaTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchPersonaFisicaById(@PathVariable Long id) {
        return personaFisicaService.buscarContactoSiExiste(id);
    }
	
	
	
	
	
	//TEST
	//Devuelve un ejemplo de PersonaFisica
	
	@GetMapping("/test")
	public PersonaFisicaPayload altaPersonaFisicaTest(/* @Valid @RequestBody PersonaFisicaPayload payload */) {
		System.out.println("Entre aca");

		PersonaFisica m = new PersonaFisica();

		// Contacto
		m.setId((long) 1234);
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.of(1990, 1, 20));
		m.setFechaBajaContacto(LocalDate.of(2000, 1, 20));
		m.setNombreDescripcion("Persona Fisica Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setId((long) 1234);
		m.setDni(12345678);
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));
		m.setEstadoActivoPersonaFisica(true);
		
		return m.toPayload();
	}
	
	@GetMapping("/test/aleatorio")
	public PersonaFisicaPayload personaFisicaAleatoriaTest(/* @Valid @RequestBody PersonaFisicaPayload payload */) {
		return personaFisicaService.personaFisicaGenerator();
	}
}
