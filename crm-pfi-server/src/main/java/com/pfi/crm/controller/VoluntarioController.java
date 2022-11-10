package com.pfi.crm.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.Voluntario;
import com.pfi.crm.multitenant.tenant.payload.VoluntarioPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.VoluntarioNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.VoluntarioService;

@RestController
@RequestMapping("/api/voluntario")
public class VoluntarioController {
	
	@Autowired
	private VoluntarioService voluntarioService;
	
	
	
	@GetMapping("/{id}")
    public VoluntarioPayload getVoluntarioById(@PathVariable Long id) {
        return voluntarioService.getVoluntarioByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<VoluntarioPayload> getVoluntario() {
    	return  voluntarioService.getVoluntarios();
	}
	
	@PostMapping({"/", "/alta"})
    public VoluntarioPayload altaVoluntario(@Valid @RequestBody VoluntarioPayload payload) {
    	return voluntarioService.altaVoluntario(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaVoluntario(@PathVariable Long id) {
		voluntarioService.bajaVoluntario(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public VoluntarioPayload modificarVoluntario(@Valid @RequestBody VoluntarioPayload payload) {
    	return voluntarioService.modificarVoluntario(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new VoluntarioNombreTablaPayload().getNombresVoluntarioTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload

	@GetMapping("/test")
	public VoluntarioPayload altaVoluntarioTest(/* @Valid @RequestBody VoluntarioPayload payload */) {

		Voluntario m = new Voluntario();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Voluntario Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		//Voluntario
		//No tiene otros atributos

		return m.toPayload();
	}
}
