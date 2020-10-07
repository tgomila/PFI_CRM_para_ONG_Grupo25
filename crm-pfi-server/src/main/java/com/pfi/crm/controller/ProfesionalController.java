package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.model.Profesional;
import com.pfi.crm.payload.ProfesionalPayload;
import com.pfi.crm.service.ProfesionalService;

@RestController
@RequestMapping("/api/profesional")
public class ProfesionalController {
	
	@Autowired
	private ProfesionalService profesionalService;
	
	
	
	@GetMapping("/{id}")
    public ProfesionalPayload getProfesionalById(@PathVariable Long id) {
        return profesionalService.getProfesionalByIdContacto(id);
    }
	
	@GetMapping("/all")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<ProfesionalPayload> getProfesional() {
    	return  profesionalService.getPersonasFisicas();
	}
	
	@PostMapping({"/", "/alta"})
    public ProfesionalPayload altaProfesional(@Valid @RequestBody ProfesionalPayload payload) {
    	return profesionalService.altaProfesional(payload);
    }
	
	@PostMapping({"/baja/{id}"})
    public void bajaProfesional(@PathVariable Long id) {
		profesionalService.bajaProfesional(id);
    }
	
	@PostMapping("/modificar")
    public ProfesionalPayload modificarProfesional(@Valid @RequestBody ProfesionalPayload payload) {
    	return profesionalService.modificarProfesional(payload);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de PersonaFisica

	@GetMapping("/test")
	public ProfesionalPayload altaProfesionalTest(/* @Valid @RequestBody ProfesionalPayload payload */) {

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
