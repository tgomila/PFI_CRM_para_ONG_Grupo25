package com.pfi.crm.controller;

import java.util.ArrayList;
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

import com.pfi.crm.multitenant.tenant.model.PersonaJuridica;
import com.pfi.crm.multitenant.tenant.model.TipoPersonaJuridica;
import com.pfi.crm.multitenant.tenant.payload.PersonaJuridicaPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PersonaJuridicaNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.PersonaJuridicaService;

@RestController
@RequestMapping("/api/personajuridica")
public class PersonaJuridicaController {
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	
	
	@GetMapping("/{id}")
    public PersonaJuridicaPayload getPersonaJuridicaById(@PathVariable Long id) {
        return personaJuridicaService.getPersonaJuridicaByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<PersonaJuridicaPayload> getPersonaJuridica() {
    	return  personaJuridicaService.getPersonasJuridicas();
	}
	
	@PostMapping({"/", "/alta"})
    public PersonaJuridicaPayload altaPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload) {
    	return personaJuridicaService.altaPersonaJuridica(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaPersonaJuridica(@PathVariable Long id) {
		personaJuridicaService.bajaPersonaJuridica(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public PersonaJuridicaPayload modificarPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload) {
    	return personaJuridicaService.modificarPersonaJuridica(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new PersonaJuridicaNombreTablaPayload().getNombresPersonaJuridicaTabla();
	}
	
	//Clase temporal
	class TipoPayload {
		String value;
		String label;
		public TipoPayload(String value, String label) {
			this.value = value;
			this.label = label;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		
	}
	@GetMapping({"/enum/tipo_persona_puridica"})
	public List<TipoPayload> getTipoPersonaJuridicaEnum() {
		TipoPersonaJuridica allTipos[] = TipoPersonaJuridica.values();
		List<TipoPayload> allTiposPayload = new ArrayList<TipoPayload>();
		for(int i=0; i < allTipos.length; i++) {
			allTiposPayload.add(new TipoPayload(allTipos[i].toString(), allTipos[i].getName()));
		}
		return allTiposPayload;
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchPersonaJuridicaById(@PathVariable Long id) {
        return personaJuridicaService.buscarContactoSiExiste(id);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Persona juridica

	@GetMapping("/test")
	public PersonaJuridicaPayload altaVoluntarioTest(/* @Valid @RequestBody PersonaJuridicaPayload payload */) {
		
		PersonaJuridica m = new PersonaJuridica();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Persona Fisica Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// Persona Juridica
		m.setInternoTelefono("07");
		m.setTipoPersonaJuridica(TipoPersonaJuridica.EMPRESA);

		return m.toPayload();
	}
}
