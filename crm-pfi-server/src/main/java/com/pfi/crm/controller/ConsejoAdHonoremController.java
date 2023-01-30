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

import com.pfi.crm.multitenant.tenant.model.ConsejoAdHonorem;
import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ConsejoAdHonoremNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ConsejoAdHonoremService;

@RestController
@RequestMapping("/api/consejoadhonorem")
public class ConsejoAdHonoremController {
	
	@Autowired
	private ConsejoAdHonoremService consejoAdHonoremService;
	
	
	
	@GetMapping("/{id}")
    public ConsejoAdHonoremPayload getConsejoAdHonoremById(@PathVariable Long id) {
        return consejoAdHonoremService.getConsejoAdHonoremByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ConsejoAdHonoremPayload> getConsejoAdHonorem() {
    	return  consejoAdHonoremService.getPersonasFisicas();
	}
	
	@PostMapping({"/", "/alta"})
    public ConsejoAdHonoremPayload altaConsejoAdHonorem(@Valid @RequestBody ConsejoAdHonoremPayload payload) {
    	return consejoAdHonoremService.altaConsejoAdHonorem(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaConsejoAdHonorem(@PathVariable Long id) {
		consejoAdHonoremService.bajaConsejoAdHonorem(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public ConsejoAdHonoremPayload modificarConsejoAdHonorem(@Valid @RequestBody ConsejoAdHonoremPayload payload) {
    	return consejoAdHonoremService.modificarConsejoAdHonorem(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new ConsejoAdHonoremNombreTablaPayload().getNombresConsejoAdHonoremTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de ConsejoAdHonorem payload

	@GetMapping("/test")
	public ConsejoAdHonoremPayload altaConsejoAdHonoremTest(/* @Valid @RequestBody ConsejoAdHonoremPayload payload */) {

		ConsejoAdHonorem m = new ConsejoAdHonorem();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("ConsejoAdHonorem Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// ConsejoAdHonorem
		m.setFuncion("Super consejero de la ONG");

		return m.toPayload();
	}
}
