package com.pfi.crm.controller;

import java.time.LocalDateTime;
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
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PrestamoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.PrestamoService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

	@Autowired
	private PrestamoService prestamoService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public PrestamoPayload getPrestamoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRESTAMO, "Ver préstamo con id: '" + id + "'");
		return prestamoService.getPrestamoById(id);
	}

	@GetMapping({ "/", "/all" })
	public List<PrestamoPayload> getPrestamo(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRESTAMO, "Ver préstamos");
		return prestamoService.getPrestamos();
	}

	@PostMapping({ "/", "/alta" })
	public PrestamoPayload altaPrestamo(@Valid @RequestBody PrestamoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRESTAMO, "Dar de alta un préstamo");
		return prestamoService.altaPrestamo(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	public ResponseEntity<?> bajaPrestamo(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRESTAMO, "Dar de baja un préstamo");
		String message = prestamoService.bajaPrestamo(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}

	@PutMapping({ "/", "/modificar" })
	public PrestamoPayload modificarPrestamo(@Valid @RequestBody PrestamoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRESTAMO, "Modificar préstamo");
		return prestamoService.modificarPrestamo(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRESTAMO, "Ver nombre de cada columna de la tabla préstamo");
		return new PrestamoNombreTablaPayload().getNombresPrestamoTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	@GetMapping("/test")
	public PrestamoPayload getPrestamoTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRESTAMO, "Ver un ejemplo de préstamo");
		
		PrestamoPayload p = new PrestamoPayload();
		p.setId(Long.valueOf(1234));
		p.setDescripcion("Radio a pilas Panasonic");
		p.setCantidad(1);
		p.setFechaPrestamoInicio(LocalDateTime.now());
		p.setFechaPrestamoFin(LocalDateTime.now().plusMonths(2));
		return p;
	}

}
