package com.pfi.crm.controller;

import java.time.LocalDateTime;
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
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ActividadNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ActividadService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/actividad")
public class ActividadController {

	@Autowired
	private ActividadService actividadService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public ActividadPayload getActividadById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver una actividad con id: "+id);
		return actividadService.getActividadById(id);
	}

	@GetMapping({ "/", "/all" })
	public List<ActividadPayload> getActividad(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver todas las actividades");
		return actividadService.getActividades();
	}

	@PostMapping({ "/", "/alta" })
	public ActividadPayload altaActividad(@Valid @RequestBody ActividadPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Cargar una nueva actividad");
		return actividadService.altaActividad(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	public ResponseEntity<?> bajaActividad(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Dar de baja la actividad" + id!=null ? ("con id"+id):"");
		String message = actividadService.bajaActividad(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}

	@PutMapping({ "/", "/modificar" })
	public ActividadPayload modificarActividad(@Valid @RequestBody ActividadPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Modificar una actividad" + payload!=null && payload.getId()!=null ? "con id"+payload.getId() :"");
		return actividadService.modificarActividad(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver nombres de la tabla de actividades");
		return new ActividadNombreTablaPayload().getNombresActividadTabla();
	}
	
	
	
	
	//Gráficos
	@GetMapping("/grafico/contar_top_20/beneficiarios")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countTop20Beneficiarios(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Ver top 20 beneficiarios más suscriptos a actividades");
		return actividadService.countTop20Beneficiarios();
	}
	
	@GetMapping("/grafico/ultimos_y_proximos_12_meses")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Map<String, Object>> countUltimosProximos12meses(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Ver actividades en los ultimos 12 meses por mes");
		return actividadService.countUltimosProximos12meses();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	BeneficiarioController beneficiarioController;
	
	@Autowired
	ProfesionalController profesionalController;
	
	
	@GetMapping("/test")
	public ActividadPayload getActividadTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Ver ejemplo de una actividad");
		
		ActividadPayload p = new ActividadPayload();
		p.setId(Long.valueOf(1234));
		p.setFechaHoraDesde(LocalDateTime.of(2023, 1, 1, 8, 15));
		p.setFechaHoraHasta(LocalDateTime.of(2023, 1, 1, 16, 30));
		p.agregarBeneficiario(beneficiarioController.getBeneficiarioTest(currentUser));
		p.agregarProfesional(profesionalController.getProfesionalTest(currentUser));
		p.setDescripcion("Actividad de verano");
		return p;
	}
}
