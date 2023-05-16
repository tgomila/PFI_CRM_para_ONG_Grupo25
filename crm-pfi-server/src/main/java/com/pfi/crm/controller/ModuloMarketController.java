package com.pfi.crm.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ModuloMarketNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloMarketService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/modulomarket")
public class ModuloMarketController {
	
	@Autowired
	ModuloMarketService moduloMarketService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	@GetMapping("/{id}")
	public ModuloMarketPayload getModuloMarketPayload(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver ModuloMarketplace con id: '" + id + "'");
		return moduloMarketService.getModuloMarketById(id);
	}
	
	@GetMapping("/modulo/find/{moduloEnum}")
	public ModuloMarketPayload getModuloMarketByModuloEnum(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver ModuloMarketplace sobre enum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.getModuloMarketByModuloEnum(moduloEnum);
	}
	
	@GetMapping({"/", "/all"})
	public List<ModuloMarketPayload> getModuloMarkets(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver lista de ModuloMarketplace");
		return moduloMarketService.getModuloMarkets();
	}
	
	@GetMapping({"/paid_modules"})
	public List<ModuloMarketPayload> getPaidModuloMarkets(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver lista de ModuloMarketplace que requieran suscripción");
		return moduloMarketService.getPaidModuloMarkets();
	}
	
	//@GetMapping("/modulo2str")
	//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	//public ModuloMarketPayload getModuloMarketByModuloEnum(@RequestParam("moduloEnum") ModuloEnum moduloEnum) {
	//	return moduloMarketService.getModuloMarketByModuloEnum(moduloEnum);
	//}
	
	//Suscripciones
	@GetMapping({"/suscripcion/precio/premium/mes"})
	public double precioSuscripcionPremiumMes(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver precio suscripción de 1 mes ModuloMarketplace");
		return moduloMarketService.precioSuscripcionPremiumMes();
	}

	@GetMapping({"/suscripcion/precio/premium/anio"})
	public double precioSuscripcionPremiumAnio(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver precio suscripción de 1 año ModuloMarketplace");
		return moduloMarketService.precioSuscripcionPremiumAnio();
	}
	
	@GetMapping({"/suscripcion/isPrueba7diasUtilizada"})
	public boolean isPrueba7diasUtilizada(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Consultar si se utilizó la prueba de 7 días premium marketplace");
		return moduloMarketService.isPrueba7diasUtilizada();
	}
	
	@GetMapping({"/suscripcion/isPrueba7diasUtilizada/{moduloEnum}"})
	public boolean isPrueba7diasUtilizada(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver precio suscripción de 1 año ModuloMarketplace en moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.isPrueba7diasUtilizada(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/activarPrueba7dias/{moduloEnum}"})
	public ModuloMarketPayload activarPrueba7dias(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar 7 días de prueba premium en moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.activarPrueba7dias(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/activarPrueba7dias"})
	public List<ModuloMarketPayload> activarPrueba7dias(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar 7 días de prueba premium en todos los módulos");
		return moduloMarketService.activarPrueba7dias();
	}
	
	@PostMapping({"/suscripcion/mes/{moduloEnum}"})
	public ModuloMarketPayload suscripcionBasicMes(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar suscripción de 1 mes premium en moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.suscripcionBasicMes(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/anio/{moduloEnum}"})
	public ModuloMarketPayload suscripcionBasicAnio(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar suscripción de 1 año premium en moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.suscripcionBasicAnio(moduloEnum);
	}
	
	@PostMapping({"/suscripcion/premium/mes"})
	public List<ModuloMarketPayload> suscripcionPremiumMes(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar 1 mes de premium en todos los módulos marketplace");
		return moduloMarketService.suscripcionPremiumMes();
	}
	
	@PostMapping({"/suscripcion/premium/anio"})
	public List<ModuloMarketPayload> suscripcionPremiumAnio(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Activar 1 año de premium en todos los módulos marketplace");
		return moduloMarketService.suscripcionPremiumAnio();
	}
	
	@PostMapping({"/suscripcion/premium/desuscribir"})
	public List<ModuloMarketPayload> desuscribir(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Desuscribir premium en todos los módulos de marketplace");
		return moduloMarketService.desuscribir();
	}
	
	@PostMapping({"/suscripcion/premium/desuscribirEn5min"})
	public List<ModuloMarketPayload> desuscribirEn5min(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Desuscribir premium en todos los módulos de marketplace en 5 minutos");
		return moduloMarketService.desuscribirEn5min();
	}
	
	@GetMapping({"/modulo/acceso/{moduloEnum}"})
	public Boolean poseeAcceso(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Consultar si la organización posee acceso al moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.poseeAcceso(moduloEnum);
	}
	
	@GetMapping({"/modulo/suscripto/{moduloEnum}"})
	public Boolean poseeSuscripcionActiva(@PathVariable("moduloEnum") ModuloEnum moduloEnum, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Consultar si la organización posee suscripción al moduloEnum: '" + moduloEnum.getName() + "'");
		return moduloMarketService.poseeSuscripcionActiva(moduloEnum);
	}
	
	@GetMapping({"/modulo/vencido"})
	public List<ModuloMarketPayload> getModuloMarketVencidosByBoolean(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Consultar los módulos vencidos");
		return moduloMarketService.getModuloMarketVencidosByBoolean();
	}
	
	
	
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.MARKETPLACE, "Ver nombre de tablas de marketplace");
		return new ModuloMarketNombreTablaPayload().getNombresModuloMarketTabla();
	}
	
	
}
