package com.pfi.crm.multitenant.tenant.service;

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
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;
import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ModuloVisibilidadPorRolRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;
import com.pfi.crm.security.UserPrincipal;

@Service
public class ModuloVisibilidadPorRolService {
	
	@Autowired
	private ModuloVisibilidadPorRolRepository moduloVisibilidadPorRolRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ModuloMarketService moduloMarketService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ModuloVisibilidadPorRolService.class);
	
	public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id) {
		return moduloVisibilidadPorRolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "id", id));
	}
	
	public ModuloPayload getModulosVisibilidadPorRol(UserPrincipal currentUser) {
		User user = userRepository.getOne(currentUser.getId());
		//Set<RoleName> roles = new HashSet<RoleName>();
		//user.getRoles().forEach((rol) -> roles.add(rol.getRoleName()));
		
		RoleName rolSuperior = user.getRoleMasValuado();
		System.out.println("El rol superior es: " + rolSuperior.getName());
		
		
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleName(rolSuperior);
		if(optional.isPresent()) {   //Si existe
			return optional.get().toPayload();
		}
		return null;
	}
	
	public ModuloPayload getModulosVisibilidadPorRol(RoleName roleName){
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleName(roleName);
		if(optional.isPresent()) {   //Si existe
			return optional.get().toPayload();
		}
		return null;
    }
	
	public List<ModuloPayload> getModulosVisibilidadPorRol(){
		return moduloVisibilidadPorRolRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public ModuloPayload altaModuloVisibilidadPorRol(ModuloPayload moduloPayload) {
		RoleName roleName = RoleName.valueOf(moduloPayload.getRol());
		Role rol = roleRepository.findByName(roleName).orElseThrow(
				() -> new ResourceNotFoundException("Role", "role_name", moduloPayload.getRol()));
		
		//Verifico que no esté de alta el módulo con mismo RoleName antes del alta
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleName(roleName);
		if(optional.isPresent()) {   //Si existe
			throw new BadRequestException("Módulo visibilidad por rol " + roleName.toString() + " ya está dado de alta.");
		}
		
		//Si no existe el modulo con roleName, lo doy de alta.
		ModuloVisibilidadPorRol alta = new ModuloVisibilidadPorRol();
		alta.setRole(rol);
		
		for(ModuloItemPayload m: moduloPayload.getItems()) {
			alta.agregarModulo(ModuloEnum.valueOf(m.getName()), ModuloTipoVisibilidadEnum.valueOf(m.getTipoVisibilidad()));
		}
		
		return altaModuloVisibilidadPorRol(alta);
	}
	
	public ModuloPayload altaModuloVisibilidadPorRol(ModuloVisibilidadPorRol moduloVisibilidadPorRol) {
		//No permito el alta de una visibilidad vencida
		List<ModuloMarketPayload> moduloMarketVencidos = moduloMarketService.getModuloMarketVencidos();
		for(ModuloMarketPayload moduloMarket: moduloMarketVencidos) {
			moduloVisibilidadPorRol.quitarModulo(moduloMarket.getModuloEnum());
		}
		//Luego de corroborar que no haya visibilidad vencida, lo doy de alta
		moduloVisibilidadPorRol.setId(null);
		return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol).toPayload();
	}
	
	public boolean bajaModuloVisibilidadPorRol(Long id) {
		try {
			moduloVisibilidadPorRolRepository.deleteById(id);
			return true;
		} catch (Exception ex) {
			logger.error("No se pudo realizar la baja de ModuloVisibilidadPorRol id:" + id + " ", ex);
			return false;
		}
	}
	
	public void quitarVisibilidadModulo(ModuloEnum moduloEnum) {
		List<ModuloVisibilidadPorRol> moduloVisibilidadPorRols = moduloVisibilidadPorRolRepository.findAll();
		for(ModuloVisibilidadPorRol modulo: moduloVisibilidadPorRols) {
			boolean deboGuardar = modulo.quitarModulo(moduloEnum);
			if(deboGuardar)
				moduloVisibilidadPorRolRepository.save(modulo);
		}
		
	}
	
	//public ModuloVisibilidadPorRol modificarModuloVisibilidadPorRol(ModuloPayload payload) {
	//	ModuloVisibilidadPorRol model = new ModuloVisibilidadPorRol(payload);
	//	if (moduloVisibilidadPorRol != null && moduloVisibilidadPorRol.getId() != null)
	//		return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol);
	//	else
	//		return null;
	//}
	
}
