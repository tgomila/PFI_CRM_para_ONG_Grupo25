package com.pfi.crm.controller;

import java.time.LocalDate;
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
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PersonaFisicaNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.PersonaFisicaService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;



@RestController
@RequestMapping("/api/personafisica")
public class PersonaFisicaController {
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
	public PersonaFisicaPayload getPersonaFisicaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Ver persona con id: '" + id + "'");
		return personaFisicaService.getPersonaFisicaByIdContacto(id);
	}
	
	@GetMapping("/si_existe/{id}")
	public PersonaFisicaPayload getSiExistePersonaFisicaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Ver persona con id: '" + id + "'");
		boolean existe = personaFisicaService.existePersonaFisicaPorIdContacto(id);
		return existe ? personaFisicaService.getPersonaFisicaByIdContacto(id) : null;
	}
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
	public List<PersonaFisicaPayload> getPersonaFisica(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Ver personas");
		return  personaFisicaService.getPersonasFisicas();
	}
	
	@PostMapping({"/", "/alta"})
	public PersonaFisicaPayload altaPersonaFisica(@Valid @RequestBody PersonaFisicaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONA, "Dar de alta una persona");
		return personaFisicaService.altaPersonaFisica(payload);
	}
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
	public ResponseEntity<?> bajaPersonaFisica(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONA, "Dar de baja persona");
		String message = personaFisicaService.bajaPersonaFisica(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}
	
	@PutMapping({"/", "/modificar"})
	public PersonaFisicaPayload modificarPersonaFisica(@Valid @RequestBody PersonaFisicaPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONA, "Modificar una persona");
		return personaFisicaService.modificarPersonaFisica(payload);
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Ver nombre de cada columna de tabla persona");
		return new PersonaFisicaNombreTablaPayload().getNombresPersonaFisicaTabla();
	}
	
	//Devuelve dto de contacto (si existe), o not found, o error si existe persona (se usa para alta persona y asociar contacto). 
	@GetMapping("/search/{id}")
	public ResponseEntity<?> searchPersonaFisicaById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Buscar contacto si existe");
		return personaFisicaService.buscarContactoSiExiste(id);
	}
	
	//Devuelve dto de persona o contacto (si existe), o not found 
	@GetMapping("/search_persona_contacto/{id}")
	public ResponseEntity<?> searchPersonaFisicaOContactoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Buscar una persona/contacto");
		return personaFisicaService.buscarPersonaOContactoSiExiste(id);
	}
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONA, "Ver personas creadas en los ultimos 12 meses por mes");
		return personaFisicaService.countCreadosUltimos12meses();
	}
	
	@GetMapping("/grafico/contar_categoria_edad")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PERSONA, "Contar cuántas personas hay, por categoría de edad");
		return personaFisicaService.obtenerConteoPorEtapasEdad();
	}
	
	
	
	
	
	//TEST
	//Devuelve un ejemplo de PersonaFisica
	
	@GetMapping("/test")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public PersonaFisicaPayload getPersonaFisicaTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Testing ver un ejemplo de persona");
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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public PersonaFisicaPayload personaFisicaAleatoriaTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Testing ver un ejemplo de persona aleatoria");
		return personaFisicaService.personaFisicaGenerator();
	}
	
	@GetMapping("/test/aleatorio/{edad}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public PersonaFisicaPayload personaFisicaAleatoriaTest(@PathVariable Integer edad, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PERSONA, "Testing ver un ejemplo de persona aleatoria");
		return personaFisicaService.personaFisicaGenerator(edad, edad, null);
	}
}
