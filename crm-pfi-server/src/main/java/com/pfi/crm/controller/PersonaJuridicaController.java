package com.pfi.crm.controller;

import java.util.ArrayList;
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
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.PersonaJuridica;
import com.pfi.crm.multitenant.tenant.model.TipoPersonaJuridica;
import com.pfi.crm.multitenant.tenant.payload.PersonaJuridicaPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PersonaJuridicaNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.PersonaJuridicaService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/personajuridica")
public class PersonaJuridicaController {
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
	public PersonaJuridicaPayload getPersonaJuridicaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Ver PersonaJuridica con id: '" + id + "'");
		return personaJuridicaService.getPersonaJuridicaByIdContacto(id);
	}
	
	@GetMapping({"/", "/all"})
	public List<PersonaJuridicaPayload> getPersonaJuridica(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Ver lista completa de PersonaJuridicas");
		return  personaJuridicaService.getPersonasJuridicas();
	}
	
	@PostMapping({"/", "/alta"})
	public PersonaJuridicaPayload altaPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONAJURIDICA, "Cargar una PersonaJuridica");
		return personaJuridicaService.altaPersonaJuridica(payload);
	}
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
	public ResponseEntity<?> bajaPersonaJuridica(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONAJURIDICA, "Baja de PersonaJuridica con id: '" + id + "'");
		String message = personaJuridicaService.bajaPersonaJuridica(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}
	
	@PutMapping({"/", "/modificar"})
	public PersonaJuridicaPayload modificarPersonaJuridica(@Valid @RequestBody PersonaJuridicaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONAJURIDICA, "Modificar una PersonaJuridica");
		return personaJuridicaService.modificarPersonaJuridica(payload);
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Ver nombre de columna de tablas de PersonaJuridica");
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
	public List<TipoPayload> getTipoPersonaJuridicaEnum(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Ver tipo enum de PersonaJuridica");
		TipoPersonaJuridica allTipos[] = TipoPersonaJuridica.values();
		List<TipoPayload> allTiposPayload = new ArrayList<TipoPayload>();
		for(int i=0; i < allTipos.length; i++) {
			allTiposPayload.add(new TipoPayload(allTipos[i].toString(), allTipos[i].getName()));
		}
		return allTiposPayload;
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
	public ResponseEntity<?> searchPersonaJuridicaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Buscar una PersonaJuridica con id: '" + id + "'");
		return personaJuridicaService.buscarContactoSiExiste(id);
	}
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONAJURIDICA, "Ver personas jurídicas creados en los ultimos 12 meses por mes");
		return personaJuridicaService.countCreadosUltimos12meses();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Persona juridica

	@GetMapping("/test")
	public PersonaJuridicaPayload altaPersonaJuridicaTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONAJURIDICA, "Ver un ejemplo de PersonaJuridica");
		
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
