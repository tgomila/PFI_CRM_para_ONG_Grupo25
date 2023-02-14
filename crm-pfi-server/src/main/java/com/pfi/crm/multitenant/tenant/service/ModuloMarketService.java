package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloMarket;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ModuloMarketRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;

@Service
public class ModuloMarketService {
	
	@Autowired
	private ModuloMarketRepository moduloMarketRepository;
	
	@Autowired
	private ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuloMarketService.class);
	
	public ModuloMarketPayload getModuloMarketById(@PathVariable Long id) {
		return moduloMarketRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ModuloMarket", "id", id)).toPayload();
	}
	
	public List<ModuloMarketPayload> getModuloMarkets() {
		return moduloMarketRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public ModuloMarketPayload altaModuloMarket(ModuloMarket moduloMarket) {
		moduloMarket.setId(null);
		boolean existeModuloEnum = moduloMarketRepository.existsByModuloEnum(moduloMarket.getModuloEnum());
		if(!existeModuloEnum) {
			return moduloMarketRepository.save(moduloMarket).toPayload();
		} else {
			throw new BadRequestException("ModuloMarket " + moduloMarket.getModuloEnum().getName() + " ya existe.");
		}
	}
	
	/**
	 * Incluye suscripción a su módulo
	 * @param moduloMarket
	 * @return ModuloMarketPayload guardado
	 */
	private ModuloMarketPayload modificarModuloMarket(ModuloMarket moduloMarket) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloMarket.getModuloEnum());
		moduloMarket.setId(m.getId());
		//Si acabo de cambiar las fechas de no tener suscripción a tener suscripción, suscribo.
		if(m.poseeSuscripcionVencida() && moduloMarket.poseeSuscripcionActiva()) {
			moduloVisibilidadPorRolService.suscripcion(moduloMarket.getModuloEnum());
			m.setSuscripcionActiva(true);
		}
		else if(m.poseeSuscripcionActiva() && moduloMarket.poseeSuscripcionVencida()) {
			moduloVisibilidadPorRolService.desuscripcion(moduloMarket.getModuloEnum());
			m.setSuscripcionActiva(false);
		}
		m.setPrueba7DiasUtilizada(moduloMarket.isPrueba7DiasUtilizada());
		m.setFechaPrueba7DiasUtilizada(moduloMarket.getFechaPrueba7DiasUtilizada());
		m.setFechaMaximaSuscripcion(moduloMarket.getFechaMaximaSuscripcion());
		return moduloMarketRepository.save(moduloMarket).toPayload();
	}
	
	public ModuloMarketPayload getModuloMarketByModuloEnum(ModuloEnum moduloEnum) {
		return getModuloMarketByModuloEnum(moduloEnum);
	}
	
	private  ModuloMarket getModuloMarketModelByModuloEnum(ModuloEnum moduloEnum) {
		return moduloMarketRepository.findByModuloEnum(moduloEnum).orElseThrow(
                () -> new ResourceNotFoundException("ModuloMarket", "moduloEnum name: ", moduloEnum.getName()));
	}
	
	//Suscripciones
	public double precioSuscripcionPremiumMes() {
		List<ModuloMarket> modulos = getModulosPagos();
		double precio = 0.00;
		precio = modulos.stream().filter(m -> m.isPaidModule()).mapToDouble(modulo -> modulo.getModuloEnum().getPriceOneMonth()).sum();
		//for(ModuloMarket m: modulos) {
		//	if(!m.isFreeModule())
		//		precio = precio + m.getModuloEnum().getPriceOneMonth();
		//}
		return precio*0.60;//Cobrar 60% por premium, o 40% de descuento del total de módulos
	}
	
	public double precioSuscripcionPremiumAnio() {
		List<ModuloMarket> modulos = getModulosPagos();
		double precio = 0.00;
		precio = modulos.stream().filter(m -> m.isPaidModule()).mapToDouble(modulo -> modulo.getModuloEnum().getPriceOneYear()).sum();
		//for(ModuloMarket m: modulos) {
		//	if(!m.isFreeModule())
		//		precio = precio + m.getModuloEnum().getPriceOneYear();
		//}
		return precio*0.60;//Cobrar 60% por premium, o 40% de descuento del total de módulos
	}
	
	public ModuloMarketPayload activarPrueba7dias(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(m.isPrueba7DiasUtilizada())
			if(m.getFechaPrueba7DiasUtilizada() != null)
				throw new BadRequestException("La prueba gratuita de 7 días ya ha sido "
						+ "utilizada el día " + m.getFechaPrueba7DiasUtilizada().toString());
			else
				throw new BadRequestException("La prueba gratuita de 7 días ya ha sido utilizada");
		
		m.activarSieteDiasGratis();
		return this.modificarModuloMarket(m);
	}
	
	public List<ModuloMarketPayload> activarPrueba7dias() {
		List<ModuloMarket> modulos = moduloMarketRepository.findAll();
		List<ModuloMarketPayload> modulosModificados = new ArrayList<ModuloMarketPayload>();
		if(modulos.isEmpty())
			return new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket m: modulos) {
			if(m.isPrueba7DiasUtilizada()) {
				if(m.getFechaPrueba7DiasUtilizada() != null)
					LOGGER.info("La prueba gratuita de 7 días ya ha sido "
							+ "utilizada el día " + m.getFechaPrueba7DiasUtilizada().toString());
				else
					LOGGER.info("La prueba gratuita de 7 días ya ha sido utilizada");
			}
			else {
				m.activarSieteDiasGratis();
				modulosModificados.add(this.modificarModuloMarket(m));
			}
		}
		return modulosModificados;
	}
	
	public ModuloMarketPayload suscripcionBasicMes(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(!m.isFreeModule()) {
			m.sumarUnMes();
			return this.modificarModuloMarket(m);
		}
		else
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
	}
	
	public ModuloMarketPayload suscripcionBasicAnio(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(!m.isFreeModule()) {
			m.sumarUnAnio();
			return this.modificarModuloMarket(m);
		}
		else
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
		
	}
	
	private List<ModuloMarket> getModulosPagos() {
		List<ModuloMarket> modulos = moduloMarketRepository.findAll();
		List<ModuloMarket> modulosPagos = new ArrayList<ModuloMarket>();
		for(ModuloMarket m: modulos) {
			if(!m.isFreeModule()) {
				modulosPagos.add(m);
			}
		}
		return modulosPagos;
	}
	
	public List<ModuloMarketPayload> suscripcionPremiumMes() {
		List<ModuloMarket> modulos = getModulosPagos();
		List<ModuloMarketPayload> modulosSuscriptos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket m: modulos) {
			m.sumarUnMes();
			modulosSuscriptos.add(modificarModuloMarket(m));
		}
		return modulosSuscriptos;
	}
	
	public List<ModuloMarketPayload> suscripcionPremiumAnio() {
		List<ModuloMarket> modulos = getModulosPagos();
		List<ModuloMarketPayload> modulosSuscriptos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket m: modulos) {
			m.sumarUnAnio();
			modulosSuscriptos.add(modificarModuloMarket(m));
		}
		return modulosSuscriptos;
	}
	
	//Se ejecutará este método en Event.java 1 vez por día a la medianoche
	public void comprobarSuscripciones() {
		if(!cargoBienTenant()) {
			return;
		}
		chequearYDarDeAltaModulosMarket(); //Ejecuto ante la duda
		
		List<ModuloMarket> modulos = moduloMarketRepository.findAll();
		List<ModuloMarket> modificar = new ArrayList<ModuloMarket>();
		for(ModuloMarket modulo: modulos) {
			
			//Si acabo de cambiar las fechas de no tener suscripción a tener suscripción, suscribo.
			if(!modulo.isSuscripcionActiva() && modulo.poseeSuscripcionActiva()) {
				moduloVisibilidadPorRolService.suscripcion(modulo.getModuloEnum());
				modulo.setSuscripcionActiva(true);
				modificar.add(modulo);
				LOGGER.info("Se cambiará la suscripción de " + modulo.getModuloEnum().toString() + " y ModuloVisibilidadPorRolTipo a suscripción activa.");
			}
			//Si acabo de cambiar las fechas de tener suscripción a no tener suscripción, desuscribo.
			else if(modulo.isSuscripcionActiva() && modulo.poseeSuscripcionVencida()) {
				moduloVisibilidadPorRolService.desuscripcion(modulo.getModuloEnum());
				modulo.setSuscripcionActiva(false);
				modificar.add(modulo);
				LOGGER.info("Se cambiará la suscripción de " + modulo.getModuloEnum().toString() + " y ModuloVisibilidadPorRolTipo a suscripción vencida.");
			}
			else
				LOGGER.info("La suscripción de " + modulo.getModuloEnum().toString() + " no cambiará. Se encuentra ok");
		}
		if(!modificar.isEmpty()) {
			moduloMarketRepository.saveAll(modificar);
			LOGGER.info("Se ha cambiado suscripciones activas o vencidas");
		}
	}
	
	/**
	 * 
	 * @return True si ha cargado bien el tenant, false si no lo ha hecho (Multitenancy)
	 */
	private boolean cargoBienTenant() {
		Optional<Role> rol = roleRepository.findByRoleName(RoleName.ROLE_USER);
		if (rol.isPresent()) {
			return true;
		}
		else {
			String tenantName = DBContextHolder.getCurrentDb();
			LOGGER.info("Chequear tenant '" + tenantName + "' mal cargado");
			return false;
		}
	}
	
	/**
	 * 
	 * @param moduloEnum
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public Boolean poseeSuscripcionActiva(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return moduloMarket.isSuscripcionActiva();//moduloMarket.poseeSuscripcionActiva();
	}
	
	/**
	 * 
	 * @param moduloEnum
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public Boolean poseeSuscripcionVencida(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return !moduloMarket.isSuscripcionActiva();//moduloMarket.poseeSuscripcionVencida();
	}
	
	/**
	 * 
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public List<ModuloMarketPayload> getModuloMarketVencidos() {
		List<ModuloMarket> moduloMarkets = moduloMarketRepository.findAll();
		List<ModuloMarketPayload> moduloMarketsVencidos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket modulo: moduloMarkets) {
			if(!modulo.isSuscripcionActiva())//modulo.poseeSuscripcionVencida())
				moduloMarketsVencidos.add(modulo.toPayload());
		}
		return moduloMarketsVencidos;
	}
	
	
	public List<ModuloMarketPayload> chequearYDarDeAltaModulosMarket() {
		List<ModuloEnum> modulosDeAlta = moduloMarketRepository.findAll().stream().map(ModuloMarket::getModuloEnum).collect(Collectors.toList());
		List<ModuloEnum> todosLosModulos = Arrays.asList(ModuloEnum.values());
		
		List<ModuloEnum> modulosFaltantesADarDeAlta = new ArrayList<>(todosLosModulos);
		modulosFaltantesADarDeAlta.removeAll(modulosDeAlta);
		
		if(!modulosFaltantesADarDeAlta.isEmpty()) {
			List<ModuloMarket> auxDarDeAltaTodoJunto = modulosFaltantesADarDeAlta.stream()
					.map(e -> new ModuloMarket(e)).collect(Collectors.toList());
			
			//Guardo todo junto, para que sea 1 consula y sea algo más rápido.
			List<ModuloMarketPayload> dadosDeAlta = moduloMarketRepository.saveAll(auxDarDeAltaTodoJunto).stream()
					.map(e -> e.toPayload()).collect(Collectors.toList());
			
			//List<ModuloMarketPayload> dadosDeAlta = modulosFaltantesADarDeAlta.stream()
			//		.map(e -> moduloMarketRepository.save(new ModuloMarket(e)).toPayload())
			//		.collect(Collectors.toList());
			
			return dadosDeAlta;
		}
		else
			return new ArrayList<ModuloMarketPayload>();
		
	}
	
	
	
	/*public ModuloMarketPayload toPayload(ModuloMarket m) {
		ModuloMarketPayload p = new ModuloMarketPayload();
		p.setId(m.getId());
		p.setModuloEnum(m.getModuloEnum());
		p.setPrueba7DiasUtilizada(m.isPrueba7DiasUtilizada());
		p.setFechaPrueba7DiasUtilizada(m.getFechaPrueba7DiasUtilizada());
		p.setFechaMaximaSuscripcion(m.getFechaMaximaSuscripcion());
		p.setSuscripcionActiva(m.isSuscripcionActiva());
		return p;
	}*/
}
