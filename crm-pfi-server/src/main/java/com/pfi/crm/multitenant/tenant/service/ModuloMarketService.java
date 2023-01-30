package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloMarket;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ModuloMarketRepository;

@Service
public class ModuloMarketService {
	
	@Autowired
	private ModuloMarketRepository moduloMarketRepository;
	
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
	
	private ModuloMarketPayload modificarModuloMarket(ModuloMarket moduloMarket) {
		ModuloMarket m = getModuloMarketModelByModuloEnum(moduloMarket.getModuloEnum());
		moduloMarket.setId(m.getId());
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
	
	public Boolean poseeSuscripcionActiva(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return moduloMarket.poseeSuscripcionActiva();
	}
	
	public Boolean poseeSuscripcionVencida(ModuloEnum moduloEnum) {
		ModuloMarket moduloMarket = getModuloMarketModelByModuloEnum(moduloEnum);
		return moduloMarket.poseeSuscripcionVencida();
	}
	
	public List<ModuloMarketPayload> getModuloMarketVencidos() {
		List<ModuloMarket> moduloMarkets = moduloMarketRepository.findAll();
		List<ModuloMarketPayload> moduloMarketsVencidos = new ArrayList<ModuloMarketPayload>();
		for(ModuloMarket modulo: moduloMarkets) {
			if(modulo.poseeSuscripcionVencida())
				moduloMarketsVencidos.add(modulo.toPayload());
		}
		return moduloMarketsVencidos;
	}
	
	
	public List<ModuloMarketPayload> chequearYDarDeAltaModulos() {
		List<ModuloEnum> modulosDeAlta = moduloMarketRepository.findAll().stream().map(ModuloMarket::getModuloEnum).collect(Collectors.toList());
		List<ModuloEnum> todosLosModulos = Arrays.asList(ModuloEnum.values());
		
		List<ModuloEnum> modulosFaltantesADarDeAlta = new ArrayList<>(todosLosModulos);
		modulosFaltantesADarDeAlta.removeAll(modulosDeAlta);
		
		List<ModuloMarketPayload> dadosDeAlta = modulosDeAlta.stream()
				.map(e -> moduloMarketRepository.save(new ModuloMarket(e)).toPayload())
				.collect(Collectors.toList());
		
		return dadosDeAlta;
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
