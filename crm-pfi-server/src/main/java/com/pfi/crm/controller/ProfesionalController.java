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
import com.pfi.crm.multitenant.tenant.model.Profesional;
import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ProfesionalNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.ProfesionalService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/profesional")
public class ProfesionalController {
	
	@Autowired
	private ProfesionalService profesionalService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
	public ProfesionalPayload getProfesionalById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROFESIONAL, "Ver profesional id: '" + id + "'");
		return profesionalService.getProfesionalByIdContacto(id);
	}
	
	@GetMapping({"/", "/all"})
	public List<ProfesionalPayload> getProfesional(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROFESIONAL, "Ver profesionales");
		return  profesionalService.getProfesionales();
	}
	
	@PostMapping({"/", "/alta"})
	public ProfesionalPayload altaProfesional(@Valid @RequestBody ProfesionalPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROFESIONAL, "Cargar nuevo profesional");
		return profesionalService.altaProfesional(payload);
	}
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
	public ResponseEntity<?> bajaProfesional(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROFESIONAL, "Dar de baja a profesional con id: '" + id + "'");
		String message = profesionalService.bajaProfesional(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}
	
	@PutMapping({"/", "/modificar"})
	public ProfesionalPayload modificarProfesional(@Valid @RequestBody ProfesionalPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROFESIONAL, "Modificar profesional");
		return profesionalService.modificarProfesional(payload);
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROFESIONAL, "Ver nombre de columnas de tabla profesional");
		return new ProfesionalNombreTablaPayload().getNombresProfesionalTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
	public ResponseEntity<?> searchProfesionalById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROFESIONAL, "Buscar profesional/persona/contacto con id: '" + id + "'");
		return profesionalService.buscarPersonaFisicaSiExiste(id);
	}
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_creados/ultimos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countCreadosUltimos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROFESIONAL, "Ver profesionales creados en los ultimos 12 meses por mes");
		return profesionalService.countCreadosUltimos12meses();
	}
	
	@GetMapping("/grafico/contar_categoria_edad")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> obtenerConteoPorEtapasEdad(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROFESIONAL, "Contar cuántos profesionales hay, por categoría de edad");
		return profesionalService.obtenerConteoPorEtapasEdad();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de PersonaFisica

	@GetMapping("/test")
	public ProfesionalPayload getProfesionalTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROFESIONAL, "Ejemplo de un profesional");

		Profesional m = new Profesional();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Profesional Don psicologo");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("estebanquito@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Esteban");
		m.setApellido("Quito");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Profesional
		m.setProfesion("Psicologo");
		// Fin Profesional

		return m.toPayload();
	}
}
