package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.payload.PersonaJuridicaPayload;
import com.pfi.crm.service.PersonaJuridicaService;

@RestController
@RequestMapping("/api/personajuridica")
public class PersonaJuridicaController {
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	
	
	@GetMapping("/{id}")
    public PersonaJuridicaPayload getPersonaJuridicaById(@PathVariable Long id) {
        return personaJuridicaService.getPersonaJuridicaByIdContacto(id);
    }
	
	@GetMapping("/all")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<PersonaJuridicaPayload> getPersonaJuridica() {
    	return  personaJuridicaService.getPersonasJuridicas();
	}
	
	@PostMapping({"/", "/alta"})
    public PersonaJuridicaPayload altaPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload) {
    	return personaJuridicaService.altaPersonaJuridica(payload);
    }
	
	@PostMapping({"/baja"})
    public void bajaPersonaJuridica(@PathVariable Long id) {
		personaJuridicaService.bajaPersonaJuridica(id);
    }
	
	@PostMapping("/modificar")
    public PersonaJuridicaPayload modificarPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload) {
    	return personaJuridicaService.modificarPersonaJuridica(payload);
    }
}
