package com.pfi.crm.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ModuloMarketNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloMarketService;

@RestController
@RequestMapping("/api/modulomarket")
public class ModuloMarketController {
	
	@Autowired
	ModuloMarketService moduloMarketService;
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ModuloMarketPayload getModuloMarketPayload(@PathVariable Long id) {
		return moduloMarketService.getModuloMarketById(id);
	}
	
	@GetMapping({"/", "/all"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public List<ModuloMarketPayload> getModuloMarkets() {
		return moduloMarketService.getModuloMarkets();
	}
	
	@GetMapping("/modulo/find/{moduloEnum}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ModuloMarketPayload getModuloMarketByModuloEnum(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.getModuloMarketByModuloEnum(moduloEnum);
	}
	
	//@GetMapping("/modulo2str")
	//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	//public ModuloMarketPayload getModuloMarketByModuloEnum(@RequestParam("moduloEnum") ModuloEnum moduloEnum) {
	//	return moduloMarketService.getModuloMarketByModuloEnum(moduloEnum);
	//}
	
	//Suscripciones
	@GetMapping({"/suscripcion/precio/premium/mes"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public double precioSuscripcionPremiumMes() {
		return moduloMarketService.precioSuscripcionPremiumMes();
	}

	@GetMapping({"/suscripcion/precio/premium/anio"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public double precioSuscripcionPremiumAnio() {
		return moduloMarketService.precioSuscripcionPremiumAnio();
	}
	
	@PostMapping({"/suscripcion/activarPrueba7dias/{moduloEnum}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModuloMarketPayload activarPrueba7dias(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.activarPrueba7dias(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/activarPrueba7dias"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ModuloMarketPayload> activarPrueba7dias() {
		return moduloMarketService.activarPrueba7dias();
	}
	
	@PostMapping({"/suscripcion/mes/{moduloEnum}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModuloMarketPayload suscripcionBasicMes(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.suscripcionBasicMes(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/anio/{moduloEnum}"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModuloMarketPayload suscripcionBasicAnio(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.suscripcionBasicAnio(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/premium/mes"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ModuloMarketPayload> suscripcionPremiumMes() {
		return moduloMarketService.suscripcionPremiumMes();
	}
	
	@PostMapping({"/suscripcion/premium/anio"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ModuloMarketPayload> suscripcionPremiumAnio() {
		return moduloMarketService.suscripcionPremiumAnio();
	}
	
	@PostMapping({"/suscripcion/premium/desuscribir"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ModuloMarketPayload> desuscribir() {
		return moduloMarketService.desuscribir();
	}
	
	@PostMapping({"/suscripcion/premium/desuscribirEn5min"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<ModuloMarketPayload> desuscribirEn5min() {
		return moduloMarketService.desuscribirEn5min();
	}
	
	@GetMapping({"/modulo/acceso/{moduloEnum}"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public Boolean poseeAcceso(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.poseeAcceso(moduloEnum);
	}
	
	@GetMapping({"/modulo/suscripto/{moduloEnum}"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public Boolean poseeSuscripcionActiva(@PathVariable("moduloEnum") ModuloEnum moduloEnum) {
		return moduloMarketService.poseeSuscripcionActiva(moduloEnum);
	}
	
	@GetMapping({"/modulo/vencido"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public List<ModuloMarketPayload> getModuloMarketVencidosByBoolean() {
		return moduloMarketService.getModuloMarketVencidosByBoolean();
	}
	
	
	
	
	@GetMapping({"/nombres_tabla"})
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public LinkedHashMap<String, String> getNombresTabla() {
		return new ModuloMarketNombreTablaPayload().getNombresModuloMarketTabla();
	}
	
	
}
