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

import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ActividadNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ActividadService;

@RestController
@RequestMapping("/api/actividad")
public class ActividadController {

	@Autowired
	private ActividadService actividadService;

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ActividadPayload getActividadById(@PathVariable Long id) {
		return actividadService.getActividadById(id);
	}

	@GetMapping({ "/", "/all" })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public List<ActividadPayload> getActividad() {
		return actividadService.getActividades();
	}

	@PostMapping({ "/", "/alta" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ActividadPayload altaActividad(@Valid @RequestBody ActividadPayload payload) {
		return actividadService.altaActividad(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public void bajaActividad(@PathVariable Long id) {
		actividadService.bajaActividad(id);
	}

	@PutMapping({ "/", "/modificar" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ActividadPayload modificarActividad(@Valid @RequestBody ActividadPayload payload) {
		return actividadService.modificarActividad(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla() {
		return new ActividadNombreTablaPayload().getNombresActividadTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	BeneficiarioController beneficiarioController;
	
	@Autowired
	ProfesionalController profesionalController;
	
	
	@GetMapping("/test")
	public ActividadPayload getActividadTest(/* @Valid @RequestBody ActividadPayload payload */) {
		
		ActividadPayload p = new ActividadPayload();
		p.setId(Long.valueOf(1234));
		p.setFechaHoraDesde(LocalDateTime.of(2023, 1, 1, 8, 15));
		p.setFechaHoraHasta(LocalDateTime.of(2023, 1, 1, 16, 30));
		p.agregarBeneficiario(beneficiarioController.altaBeneficiarioTest());
		p.agregarProfesional(profesionalController.altaProfesionalTest());
		p.setDescripcion("Actividad de verano");
		return p;
	}
}
