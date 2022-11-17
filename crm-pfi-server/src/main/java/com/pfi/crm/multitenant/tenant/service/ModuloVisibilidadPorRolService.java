package com.pfi.crm.multitenant.tenant.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.repository.ModuloVisibilidadPorRolRepository;

@Service
public class ModuloVisibilidadPorRolService {
	@Autowired
	private ModuloVisibilidadPorRolRepository moduloVisibilidadPorRolRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ModuloVisibilidadPorRolService.class);
	
	public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id) {
		return moduloVisibilidadPorRolRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "id", id));
	}
	
	public List<ModuloVisibilidadPorRol> getModulosVisibilidadPorRol() {
		return moduloVisibilidadPorRolRepository.findAll();
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
	
	public ModuloVisibilidadPorRol modificarModuloVisibilidadPorRol(ModuloVisibilidadPorRol moduloVisibilidadPorRol) {
		if (moduloVisibilidadPorRol != null && moduloVisibilidadPorRol.getId() != null)
			return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol);
		else
			return null;
	}
	
	public List<ModuloVisibilidadPorRol> getModuloVisibilidadPorRolByNameByRoleName(RoleName roleName){
		return null;
		//return moduloVisibilidadPorRolRepository.findByRoleName(roleName).stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	
	//TODO Payload
	
}
