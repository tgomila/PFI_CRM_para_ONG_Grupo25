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
import com.pfi.crm.multitenant.tenant.payload.ProyectoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ProyectoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ProyectoService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/proyecto")
public class ProyectoController {

	@Autowired
	private ProyectoService proyectoService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public ProyectoPayload getProyectoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROYECTO, "Ver un proyecto con id: '"+id+"'");
		return proyectoService.getProyectoById(id);
	}

	@GetMapping({ "/", "/all" })
	public List<ProyectoPayload> getProyecto(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROYECTO, "Ver todas los proyectos");
		return proyectoService.getProyectos();
	}

	@PostMapping({ "/", "/alta" })
	public ProyectoPayload altaProyecto(@Valid @RequestBody ProyectoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Cargar un nuevo proyecto");
		return proyectoService.altaProyecto(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	public ResponseEntity<?> bajaProyecto(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Dar de baja el proyecto" + id!=null ? ("con id: '"+id+"'"):"");
		String message = proyectoService.bajaProyecto(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}

	@PutMapping({ "/", "/modificar" })
	public ProyectoPayload modificarProyecto(@Valid @RequestBody ProyectoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Modificar un proyecto" + payload!=null && payload.getId()!=null ? " con id: '"+payload.getId()+"'" :"");
		return proyectoService.modificarProyecto(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROYECTO, "Ver nombres de la tabla de proyecto");
		return new ProyectoNombreTablaPayload().getNombresProyectoTabla();
	}
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_top_20/involucrados")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countTop20Beneficiarios(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Ver top 20 personas más involucrados a proyectos");
		return proyectoService.countTop20Involucrados();
	}
	
	@GetMapping("/grafico/ultimos_y_proximos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countUltimosProximos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Ver proyectos en los ultimos 12 meses por mes");
		return proyectoService.countUltimosProximos12meses();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	PersonaFisicaController personaFisicaController;
	
	
	@GetMapping("/test")
	public ProyectoPayload getProyectoTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROYECTO, "Ver ejemplo de un proyecto");
		
		ProyectoPayload p = new ProyectoPayload();
		p.setId(Long.valueOf(1234));
		p.setDescripcion("Proyecto de verano: Pintar la pared de la entrada");
		p.setFechaInicio(LocalDate.of(2023, 1, 1));
		p.setFechaFin(LocalDate.of(2023, 1, 1));
		p.agregarInvolucrado(personaFisicaController.getPersonaFisicaTest(currentUser));
		return p;
	}
}
