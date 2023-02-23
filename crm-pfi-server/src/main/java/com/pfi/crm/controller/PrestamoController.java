package com.pfi.crm.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.PrestamoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.PrestamoService;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

	@Autowired
	private PrestamoService prestamoService;

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public PrestamoPayload getPrestamoById(@PathVariable Long id) {
		return prestamoService.getPrestamoById(id);
	}

	@GetMapping({ "/", "/all" })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public List<PrestamoPayload> getPrestamo() {
		return prestamoService.getPrestamos();
	}

	@PostMapping({ "/", "/alta" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public PrestamoPayload altaPrestamo(@Valid @RequestBody PrestamoPayload payload) {
		return prestamoService.altaPrestamo(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public void bajaPrestamo(@PathVariable Long id) {
		prestamoService.bajaPrestamo(id);
	}

	@PutMapping({ "/", "/modificar" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public PrestamoPayload modificarPrestamo(@Valid @RequestBody PrestamoPayload payload) {
		return prestamoService.modificarPrestamo(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla() {
		return new PrestamoNombreTablaPayload().getNombresPrestamoTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	@GetMapping("/test")
	public PrestamoPayload getPrestamoTest(/* @Valid @RequestBody PrestamoPayload payload */) {
		
		PrestamoPayload p = new PrestamoPayload();
		p.setId(Long.valueOf(1234));
		p.setDescripcion("Radio a pilas Panasonic");
		p.setCantidad(1);
		p.setFechaPrestamoInicio(LocalDateTime.now());
		p.setFechaPrestamoFin(LocalDateTime.now().plusMonths(2));
		return p;
	}

}
