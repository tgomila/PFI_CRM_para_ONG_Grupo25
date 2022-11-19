package com.pfi.crm.multitenant.tenant.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;
import com.pfi.crm.multitenant.tenant.repository.ModuloVisibilidadPorRolRepository;
import com.pfi.crm.multitenant.tenant.repository.UserRepository;
import com.pfi.crm.security.UserPrincipal;

@Service
public class ModuloVisibilidadPorRolService {
	@Autowired
	private ModuloVisibilidadPorRolRepository moduloVisibilidadPorRolRepository;
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ModuloVisibilidadPorRolService.class);
	
	public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id) {
		return moduloVisibilidadPorRolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "id", id));
	}
	
	public ModuloPayload getModulosVisibilidadPorRol(UserPrincipal currentUser) {
		User user = userRepository.getOne(currentUser.getId());
		Set<RoleName> roles = new HashSet<RoleName>();
		user.getRoles().forEach((rol) -> roles.add(rol.getName()));
		
		RoleName rolSuperior;
		//Por prioridad
		if(roles.contains(RoleName.ROLE_ADMIN))
			rolSuperior = RoleName.ROLE_ADMIN;
		else if(roles.contains(RoleName.ROLE_EMPLOYEE))
			rolSuperior = RoleName.ROLE_EMPLOYEE;
		else if(roles.contains(RoleName.ROLE_PROFESIONAL))
			rolSuperior = RoleName.ROLE_PROFESIONAL;
		else if(roles.contains(RoleName.ROLE_USER))
			rolSuperior = RoleName.ROLE_USER;
		else {
			new ResourceNotFoundException("roles", "currentUser", currentUser);
			return null;
		}
		
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
	
	public ModuloVisibilidadPorRol altaModuloVisibilidadPorRol(ModuloVisibilidadPorRol moduloVisibilidadPorRol) {
		moduloVisibilidadPorRol.setId(null);
		return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol);
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
	
	//public ModuloVisibilidadPorRol modificarModuloVisibilidadPorRol(ModuloPayload payload) {
	//	ModuloVisibilidadPorRol model = new ModuloVisibilidadPorRol(payload);
	//	if (moduloVisibilidadPorRol != null && moduloVisibilidadPorRol.getId() != null)
	//		return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol);
	//	else
	//		return null;
	//}
	
}
