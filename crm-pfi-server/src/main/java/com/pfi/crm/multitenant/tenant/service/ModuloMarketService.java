package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDateTime;
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
	
	public List<ModuloMarketPayload> getPaidModuloMarkets() {
		return moduloMarketRepository.findAll().stream()
				.filter(m -> m.isPaidModule())
				.map(m -> m.toPayload())
				.collect(Collectors.toList());
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
		ModuloMarket modelBD = getModuloMarketModelByModuloEnum(moduloMarket.getModuloEnum());
		moduloMarket.setId(modelBD.getId());
		//Si acabo de cambiar las fechas de no tener suscripción a tener suscripción, suscribo.
		//if(modelBD.poseeSuscripcionVencida() && moduloMarket.poseeSuscripcionActiva()) {
		if(modelBD.isSuscripcionVencidaByFechas() && moduloMarket.isSuscripcionActivaByFechas()) {
			moduloVisibilidadPorRolService.suscripcion(moduloMarket.getModuloEnum());
			modelBD.setSuscripcionActiva(true);
		}
		else if(modelBD.isSuscripcionActivaByFechas() && moduloMarket.isSuscripcionVencidaByFechas()) {
			moduloVisibilidadPorRolService.desuscripcion(moduloMarket.getModuloEnum());
			modelBD.setSuscripcionActiva(false);
		}
		modelBD.setPrueba7DiasUtilizada(moduloMarket.isPrueba7DiasUtilizada());
		modelBD.setFechaPrueba7DiasUtilizada(moduloMarket.getFechaPrueba7DiasUtilizada());
		modelBD.setFechaInicioSuscripcion(moduloMarket.getFechaInicioSuscripcion());
		modelBD.setFechaMaximaSuscripcion(moduloMarket.getFechaMaximaSuscripcion());
		return moduloMarketRepository.save(moduloMarket).toPayload();
	}
	
	public ModuloMarketPayload getModuloMarketByModuloEnum(ModuloEnum moduloEnum) {
		return getModuloMarketModelByModuloEnum(moduloEnum).toPayload();
	}
	
	private  ModuloMarket getModuloMarketModelByModuloEnum(ModuloEnum moduloEnum) {
		return moduloMarketRepository.findByModuloEnum(moduloEnum).orElseThrow(
                () -> new ResourceNotFoundException("ModuloMarket", "moduloEnum name: ", moduloEnum.getName()));
	}
	
	private List<ModuloMarket> getModulosPagos() {
		List<ModuloMarket> modulos = moduloMarketRepository.findAll();
		List<ModuloMarket> modulosPagos = new ArrayList<ModuloMarket>();
		for(ModuloMarket m: modulos) {
			if(m.isPaidModule()) {
				modulosPagos.add(m);
			}
		}
		return modulosPagos;
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
	
	public boolean isPrueba7diasUtilizada() {
		List<ModuloMarket> modulos = moduloMarketRepository.findAll();
		if(modulos.isEmpty())
			return false;
		boolean pruebaUtilizada = true;
		for(ModuloMarket m: modulos) {
			if(m.isPaidModule() && !m.isPrueba7DiasUtilizada()) {
				pruebaUtilizada = false;
			}
		}
		return pruebaUtilizada;
	}
	
	public boolean isPrueba7diasUtilizada(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		return (m.isPrueba7DiasUtilizada() && m.isPaidModule());
	}
	
	public ModuloMarketPayload activarPrueba7dias(ModuloEnum moduloEnum) {
		if(moduloEnum.isFreeModule())
			throw new BadRequestException("No se puede activar período de prueba a un módulo gratuito");
		
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(m.isPrueba7DiasUtilizada())
			if(m.getFechaPrueba7DiasUtilizada() != null)
				throw new BadRequestException("La prueba gratuita de 7 días ya ha sido "
						+ "utilizada el día " + m.getFechaPrueba7DiasUtilizada().toString());
			else
				throw new BadRequestException("La prueba gratuita de 7 días ya ha sido utilizada");
		

		if(moduloEnum.equals(ModuloEnum.ACTIVIDAD)) {
			ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.PROGRAMA_DE_ACTIVIDADES);
			aux.activarSieteDiasGratis();
			this.modificarModuloMarket(aux);
		}
		else if(moduloEnum.equals(ModuloEnum.PROGRAMA_DE_ACTIVIDADES)) {
			ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.ACTIVIDAD);
			aux.activarSieteDiasGratis();
			this.modificarModuloMarket(aux);
		}
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
		modulosModificados.addAll(chequearModuloHijosConSuscripcion());
		return modulosModificados;
	}
	
	private List<ModuloMarketPayload> chequearModuloHijosConSuscripcion() {
		List<ModuloMarketPayload> modulosModificados = new ArrayList<ModuloMarketPayload>();
		ModuloMarket programa = getModuloMarketModelByModuloEnum(ModuloEnum.PROGRAMA_DE_ACTIVIDADES);
		LocalDateTime fechaMaximaPrograma = programa.getFechaMaximaSuscripcion();
		
		if(fechaMaximaPrograma != null) {//Hay suscripción al programa
			ModuloMarket actividad = getModuloMarketModelByModuloEnum(ModuloEnum.ACTIVIDAD);
			LocalDateTime fechaMaximaActividad = actividad.getFechaMaximaSuscripcion();
			
			if(fechaMaximaActividad == null || fechaMaximaPrograma.isAfter(fechaMaximaActividad)) {
				//Forzar inscribir a la actividad a la misma fecha de finalización del programa
				actividad.setFechaMaximaSuscripcion(fechaMaximaPrograma);
				modulosModificados.add(this.modificarModuloMarket(actividad));
			}
		
		}
		return modulosModificados;
	}
	
	private enum EnumAuxTiempo {
		PRUEBA_7_DIAS, _1_MES, _1_ANIO;
	}
	
	@SuppressWarnings("unused")
	private ModuloMarketPayload suscripcionBasicGeneric(ModuloEnum moduloEnum, EnumAuxTiempo tiempo) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(!m.isFreeModule()) {
			//Si es prueba gratuita, verificar si ya fue activado anteriormente o no
			if(tiempo.equals(EnumAuxTiempo.PRUEBA_7_DIAS) && m.isPrueba7DiasUtilizada()) {
				if(m.getFechaPrueba7DiasUtilizada() != null)
					throw new BadRequestException("La prueba gratuita de 7 días ya ha sido "
							+ "utilizada el día " + m.getFechaPrueba7DiasUtilizada().toString());
				else
					throw new BadRequestException("La prueba gratuita de 7 días ya ha sido utilizada");
			}
			//Inicio solo para casos de model "Padres e hijos", ejemplo Programa de Actividades
			ModuloMarket aux = null;
			switch (moduloEnum) {//Busco modulo padre/hijo para activar
				case        ACTIVIDAD:        aux = getModuloMarketModelByModuloEnum(ModuloEnum.PROGRAMA_DE_ACTIVIDADES);
				case PROGRAMA_DE_ACTIVIDADES: aux = getModuloMarketModelByModuloEnum(ModuloEnum.ACTIVIDAD);
				default:                      break;
			}
			if(aux != null) {//Activo prueba gratuita, haya sido activado o no, anteriormente
				switch (tiempo) {
					case PRUEBA_7_DIAS:	aux.forzarActivarSieteDiasGratis();
					case _1_MES:		aux.sumarUnMes();
					case _1_ANIO:		aux.sumarUnAnio();
				}
				this.modificarModuloMarket(aux);
			}
			//Fin caso padres e hijos
			switch (tiempo) {
				case PRUEBA_7_DIAS:	m.activarSieteDiasGratis();
				case _1_MES:		m.sumarUnMes();
				case _1_ANIO:		m.sumarUnAnio();
			}
			this.modificarModuloMarket(aux);
			m.sumarUnMes();
			return this.modificarModuloMarket(m);
		}
		else
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
	}
	
	public ModuloMarketPayload suscripcionBasicMes(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(!m.isFreeModule()) {
			if(moduloEnum.equals(ModuloEnum.ACTIVIDAD)) {
				ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.PROGRAMA_DE_ACTIVIDADES);
				aux.sumarUnMes();
				this.modificarModuloMarket(aux);
			}
			else if(moduloEnum.equals(ModuloEnum.PROGRAMA_DE_ACTIVIDADES)) {
				ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.ACTIVIDAD);
				aux.sumarUnMes();
				this.modificarModuloMarket(aux);
			}
			m.sumarUnMes();
			return this.modificarModuloMarket(m);
		}
		else
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
	}
	
	public ModuloMarketPayload suscripcionBasicAnio(ModuloEnum moduloEnum) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloEnum);
		if(!m.isFreeModule()) {
			if(moduloEnum.equals(ModuloEnum.ACTIVIDAD)) {
				ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.PROGRAMA_DE_ACTIVIDADES);
				aux.sumarUnAnio();
				this.modificarModuloMarket(aux);
			}
			else if(moduloEnum.equals(ModuloEnum.PROGRAMA_DE_ACTIVIDADES)) {
				ModuloMarket aux = getModuloMarketModelByModuloEnum(ModuloEnum.ACTIVIDAD);
				aux.sumarUnAnio();
				this.modificarModuloMarket(aux);
			}
			m.sumarUnAnio();
			return this.modificarModuloMarket(m);
		}
		else
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
		
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
	
	/**
	 * Warning, esto es solo si el cliente desea desuscribirse al premium, no debería suceder.
	 * Se utilizará este método en modo testing de sistema.
	 * @return módulos suscriptos, ya desuscriptos a fecha de hoy.
	 */
	public List<ModuloMarketPayload> desuscribir() {
		List<ModuloMarket> modulos = getModulosPagos();
		List<ModuloMarket> modulosModificados = new ArrayList<ModuloMarket>();
		List<ModuloMarketPayload> modulosDesuscriptos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket m: modulos) {
			if(m.isSuscripcionActivaByBoolean() || m.isSuscripcionActivaByFechas()) {
				moduloVisibilidadPorRolService.desuscripcion(m.getModuloEnum());
				m.desuscripcionPorFechas();//Por fechas
				m.setSuscripcionActiva(false);//Por boolean
				modulosModificados.add(m);
			}
		}
		if(!modulosModificados.isEmpty()) {
			List<ModuloMarket> saved = moduloMarketRepository.saveAll(modulosModificados);
			saved.forEach(s -> modulosDesuscriptos.add(s.toPayload()));
		}
		return modulosDesuscriptos;
	}
	
	//Testing
	public List<ModuloMarketPayload> desuscribirEn5min() {
		List<ModuloMarket> modulos = getModulosPagos();
		List<ModuloMarket> modulosModificados = new ArrayList<ModuloMarket>();
		List<ModuloMarketPayload> modulosDesuscriptos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket m: modulos) {
			if(m.isSuscripcionActivaByBoolean() || m.isSuscripcionActivaByFechas()) {
				m.desuscripcionEn5min();//Por fechas
				modulosModificados.add(m);
			}
		}
		if(!modulosModificados.isEmpty()) {
			List<ModuloMarket> saved = moduloMarketRepository.saveAll(modulosModificados);
			saved.forEach(s -> modulosDesuscriptos.add(s.toPayload()));
		}
		return modulosDesuscriptos;
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
			//if(!modulo.isSuscripcionActiva() && modulo.poseeSuscripcionActiva()) {
			if(modulo.suscripcionHaEmpezadoYRequiereCambiarModulosVisibilidadSinSuscripcionAOtraVista()) {
				moduloVisibilidadPorRolService.suscripcion(modulo.getModuloEnum());
				modulo.setSuscripcionActiva(true);
				modificar.add(modulo);
				LOGGER.info("Se cambiará la suscripción de " + modulo.getModuloEnum().toString() + " y ModuloVisibilidadPorRolTipo a suscripción activa.");
			}
			//Si acabo de cambiar las fechas de tener suscripción a no tener suscripción, desuscribo.
			//else if(modulo.isSuscripcionActivaByBoolean() && modulo.isSuscripcionActivaByFechas()) {
			else if(modulo.suscripcionHaVencidoYRequiereCambiarModulosVisibilidadASinSuscripcion()) {
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
	
	public boolean poseeAcceso(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		boolean premium = moduloMarket.isPaidModule();
		boolean suscripcionActiva = moduloMarket.isSuscripcionActivaByBoolean();
		boolean free = moduloMarket.isFreeModule();
		if(free || (premium && suscripcionActiva))
			return true;
		else
			return false;//premium sin suscripción
	}
	
	/**
	 * 
	 * @param moduloEnum
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public Boolean poseeSuscripcionActiva(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return moduloMarket.isSuscripcionActivaByBoolean();
	}
	
	/**
	 * 
	 * @param moduloEnum
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public boolean poseeSuscripcionVencidaByBoolean(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return moduloMarket.isSuscripcionVencidaByBoolean();//moduloMarket.poseeSuscripcionVencida();
	}
	
	/**
	 * 
	 * @return solo suscripción activa o vencida por sistema, no corrobora por fechas
	 */
	public List<ModuloMarketPayload> getModuloMarketVencidosByBoolean() {
		return moduloMarketRepository.findAll().stream()
				.filter(modulo -> modulo.isSuscripcionVencidaByBoolean())
				.map(ModuloMarket::toPayload)
				.collect(Collectors.toList());
		
		/*List<ModuloMarket> moduloMarkets = moduloMarketRepository.findAll();
		List<ModuloMarketPayload> moduloMarketsVencidos = new ArrayList<ModuloMarketPayload>();
		
		
		for(ModuloMarket modulo: moduloMarkets) {
			if(modulo.isSuscripcionVencidaByBoolean())//modulo.poseeSuscripcionVencida())
				moduloMarketsVencidos.add(modulo.toPayload());
		}
		return moduloMarketsVencidos;*/
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
					.map(ModuloMarket::toPayload).collect(Collectors.toList());
			
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
