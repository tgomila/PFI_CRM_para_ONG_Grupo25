package com.pfi.crm.controller;

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

import com.pfi.crm.multitenant.tenant.payload.InsumoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.InsumoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.InsumoService;

@RestController
@RequestMapping("/api/insumo")
public class InsumoController {

	@Autowired
	private InsumoService insumoService;

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public InsumoPayload getInsumoById(@PathVariable Long id) {
		return insumoService.getInsumoById(id);
	}

	@GetMapping({ "/", "/all" })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public List<InsumoPayload> getInsumo() {
		return insumoService.getInsumos();
	}

	@PostMapping({ "/", "/alta" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public InsumoPayload altaInsumo(@Valid @RequestBody InsumoPayload payload) {
		return insumoService.altaInsumo(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public void bajaInsumo(@PathVariable Long id) {
		insumoService.bajaInsumo(id);
	}

	@PutMapping({ "/", "/modificar" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public InsumoPayload modificarInsumo(@Valid @RequestBody InsumoPayload payload) {
		return insumoService.modificarInsumo(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla() {
		return new InsumoNombreTablaPayload().getNombresInsumoTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	@GetMapping("/test")
	public InsumoPayload getInsumoTest(/* @Valid @RequestBody InsumoPayload payload */) {
		
		InsumoPayload p = new InsumoPayload();
		p.setId(Long.valueOf(1234));
		p.setTipo("Galletitas");
		p.setDescripcion("Galletitas rellenas");
		p.setStockActual(25);
		p.setFragil(false);
		return p;
	}

}
