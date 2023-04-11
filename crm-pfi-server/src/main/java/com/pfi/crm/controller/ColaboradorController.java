package com.pfi.crm.controller;

import java.time.LocalDate;
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

import com.pfi.crm.multitenant.tenant.model.Colaborador;
import com.pfi.crm.multitenant.tenant.payload.ColaboradorPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ColaboradorNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ColaboradorService;

@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController  {
	
	@Autowired
	private ColaboradorService colaboradorService;
	
	
	
	@GetMapping("/{id}")
    public ColaboradorPayload getColaboradorById(@PathVariable Long id) {
        return colaboradorService.getColaboradorByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ColaboradorPayload> getColaborador() {
    	return  colaboradorService.getColaboradores();
	}
	
	@PostMapping({"/", "/alta"})
    public ColaboradorPayload altaColaborador(@Valid @RequestBody ColaboradorPayload payload) {
    	return colaboradorService.altaColaborador(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaColaborador(@PathVariable Long id) {
		colaboradorService.bajaColaborador(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public ColaboradorPayload modificarColaborador(@Valid @RequestBody ColaboradorPayload payload) {
    	return colaboradorService.modificarColaborador(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new ColaboradorNombreTablaPayload().getNombresColaboradorTabla();
	}
	
	//Devuelve dto (si existe) de Persona, o de contacto, o not found. 
	@GetMapping("/search/{id}")
    public ResponseEntity<?> searchColaboradorById(@PathVariable Long id) {
        return colaboradorService.buscarPersonaFisicaSiExiste(id);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de colaborador payload

	@GetMapping("/test")
	public ColaboradorPayload altaColaboradorTest(/* @Valid @RequestBody ColaboradorPayload payload */) {

		Colaborador m = new Colaborador();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Colaborador Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Colaborador
		m.setArea("Area administrativa");
		// Fin Colaborador

		return m.toPayload();
	}
}
