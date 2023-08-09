package com.pfi.crm.controller;

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

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ProgramaDeActividadesPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ProgramaDeActividadesNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ProgramaDeActividadesService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/programadeactividades")
public class ProgramaDeActividadesController {

	@Autowired
	private ProgramaDeActividadesService programaDeActividadesService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public ProgramaDeActividadesPayload getProgramaDeActividadesById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Ver un programa de actividades con id: '"+id+"'");
		return programaDeActividadesService.getProgramaDeActividadesById(id);
	}

	@GetMapping({ "/", "/all" })
	public List<ProgramaDeActividadesPayload> getProgramaDeActividades(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Ver todas los programas de actividades");
		return programaDeActividadesService.getProgramasDeActividades();
	}

	@PostMapping({ "/", "/alta" })
	public ProgramaDeActividadesPayload altaProgramaDeActividades(@Valid @RequestBody ProgramaDeActividadesPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Cargar un nuevo programa de actividades");
		return programaDeActividadesService.altaProgramaDeActividades(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	public ResponseEntity<?> bajaProgramaDeActividades(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Dar de baja el programa de actividad" + id!=null ? (" con id "+id+"'"):"");
		String message = programaDeActividadesService.bajaProgramaDeActividades(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}

	@PutMapping({ "/", "/modificar" })
	public ProgramaDeActividadesPayload modificarProgramaDeActividades(@Valid @RequestBody ProgramaDeActividadesPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Modificar un programa de actividades" + payload!=null && payload.getId()!=null ? " con id: '"+payload.getId()+"'" :"");
		return programaDeActividadesService.modificarProgramaDeActividades(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Ver nombres de la tabla de programa de actividades");
		return new ProgramaDeActividadesNombreTablaPayload().getNombresProgramaDeActividadesTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	ActividadController actividadController;
	
	@Autowired
	ProfesionalController profesionalController;
	
	
	@GetMapping("/test")
	public ProgramaDeActividadesPayload getProgramaDeActividadesTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Ver ejemplo de una actividad");
		
		ProgramaDeActividadesPayload p = new ProgramaDeActividadesPayload();
		p.setId(Long.valueOf(1234));
		p.setDescripcion("Programa de actividades del verano");
		p.agregarActividadesPorSemana(2, actividadController.getActividadTest(currentUser));
		return p;
	}
}
