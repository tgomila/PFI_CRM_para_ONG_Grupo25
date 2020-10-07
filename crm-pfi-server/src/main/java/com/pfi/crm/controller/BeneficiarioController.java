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

import com.pfi.crm.model.Beneficiario;
import com.pfi.crm.payload.BeneficiarioPayload;
import com.pfi.crm.service.BeneficiarioService;

@RestController
@RequestMapping("/api/beneficiario")
public class BeneficiarioController {
	
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	
	
	@GetMapping("/{id}")
    public BeneficiarioPayload getBeneficiarioById(@PathVariable Long id) {
        return beneficiarioService.getBeneficiarioByIdContacto(id);
    }
	
	@GetMapping("/all")
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<BeneficiarioPayload> getBeneficiario() {
    	return  beneficiarioService.getPersonasFisicas();
	}
	
	@PostMapping({"/", "/alta"})
    public BeneficiarioPayload altaBeneficiario(@Valid @RequestBody BeneficiarioPayload payload) {
    	return beneficiarioService.altaBeneficiario(payload);
    }
	
	@PostMapping({"/baja/{id}"})
    public void bajaBeneficiario(@PathVariable Long id) {
		beneficiarioService.bajaBeneficiario(id);
    }
	
	@PostMapping("/modificar")
    public BeneficiarioPayload modificarBeneficiario(@Valid @RequestBody BeneficiarioPayload payload) {
    	return beneficiarioService.modificarBeneficiario(payload);
    }
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de beneficiario payload

	@GetMapping("/test")
	public BeneficiarioPayload altaBeneficiarioTest(/* @Valid @RequestBody BeneficiarioPayload payload */) {

		Beneficiario m = new Beneficiario();

		// Contacto
		m.setEstadoActivoContacto(true);
		m.setNombreDescripcion("Beneficiario Don Roque");
		m.setCuit("20-1235678-9");
		m.setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.setEmail("felipe@gmail.com");
		m.setTelefono("1234-4567");

		// PersonaFisica
		m.setNombre("Felipe");
		m.setApellido("del 8");
		m.setFechaNacimiento(LocalDate.of(1990, 1, 20));

		// Beneficiario
		m.setIdONG(Long.parseLong("001234"));
		m.setLegajo(Long.parseLong("1090555"));
		m.setLugarDeNacimiento("Lanús");
		m.setSeRetiraSolo(false);
		m.setCuidadosEspeciales("Necesita asistencia psicologica");
		m.setEscuela("Colegio Nº123");
		m.setGrado("5º grado");
		m.setTurno("Mañana");
		//this.setEstadoActivoBeneficiario(true);
		// Fin Beneficiario

		return m.toPayload();
	}
}
