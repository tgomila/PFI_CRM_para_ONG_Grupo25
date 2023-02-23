package com.pfi.crm.controller;

import java.time.LocalDateTime;
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

import com.pfi.crm.multitenant.tenant.model.Donacion;
import com.pfi.crm.multitenant.tenant.model.DonacionTipo;
import com.pfi.crm.multitenant.tenant.payload.DonacionPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.DonacionNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.DonacionService;

@RestController
@RequestMapping("/api/donacion")
public class DonacionController {
	
	@Autowired
	private DonacionService donacionService;
	
	
	
	@GetMapping("/{id}")
    public DonacionPayload getDonacionById(@PathVariable Long id) {
        return donacionService.getDonacionById(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<DonacionPayload> getDonacion() {
    	return  donacionService.getDonaciones();
	}
	
	@PostMapping({"/", "/alta"})
    public DonacionPayload altaDonacion(@Valid @RequestBody DonacionPayload payload) {
    	return donacionService.altaDonacion(payload);
    }
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
    public void bajaDonacion(@PathVariable Long id) {
		donacionService.bajaDonacion(id);
    }
	
	@PutMapping({"/", "/modificar"})
    public DonacionPayload modificarDonacion(@Valid @RequestBody DonacionPayload payload) {
    	return donacionService.modificarDonacion(payload);
    }
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new DonacionNombreTablaPayload().getNombresDonacionTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo de Donacion payload

	@GetMapping("/test")
	public DonacionPayload altaDonacionTest(/* @Valid @RequestBody DonacionPayload payload */) {

		Donacion m = new Donacion();

		// Contacto
		m.getDonante().setEstadoActivoContacto(true);
		m.getDonante().setNombreDescripcion("Donante señor feliz");
		m.getDonante().setCuit("20-1235678-9");
		m.getDonante().setDomicilio("Avenida siempre falsa 123, piso 4, depto A");
		m.getDonante().setEmail("señorfeliz@gmail.com");
		m.getDonante().setTelefono("1234-4567");

		// Donacion
		m.setFecha(LocalDateTime.now());
		m.setTipoDonacion(DonacionTipo.INSUMO);
		m.setDescripcion("Biromes para los chicos");
		// Fin Donacion

		return m.toPayload();
	}
}
