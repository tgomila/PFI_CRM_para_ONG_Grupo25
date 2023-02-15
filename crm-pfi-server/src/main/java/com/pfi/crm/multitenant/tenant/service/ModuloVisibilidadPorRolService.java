package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
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
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRolTipo;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuloVisibilidadPorRolService.class);
	
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
		
		
		return moduloVisibilidadPorRolRepository.findByRoleRoleName(rolSuperior).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "Role->RoleName", rolSuperior.toString())).toPayload();
	}
	
	public ModuloPayload getModulosVisibilidadPorRol(RoleName roleName){
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleRoleName(roleName);
		if(optional.isPresent()) {   //Si existe
			return optional.get().toPayload();
		}
		return null;
    }
	
	public List<ModuloPayload> getModulosVisibilidadPorRol(){
		return moduloVisibilidadPorRolRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public List<ModuloPayload> agregarTodosLosModulos() {
		List<ModuloVisibilidadPorRol> modulos = moduloVisibilidadPorRolRepository.findAll();
		List<ModuloPayload> dadosDeAlta = new ArrayList<ModuloPayload>();
		for(RoleName roleName: RoleName.values()) {//Recorro todos los roles para dar de alta.
			ModuloVisibilidadPorRol alta = null;
			for(ModuloVisibilidadPorRol modAlta: modulos) {//Corroboro si está dado de alta módulo con dicho rol.
				if(modAlta.getRole().getRoleName().equals(roleName)) {//Si está dado de alta, descarto su alta.
					alta = modAlta;
					LOGGER.info("Módulo visibilidad por rol " + roleName.toString() + " ya está dado de alta.");
					//throw new BadRequestException("Módulo visibilidad por rol " + roleName.toString() + " ya está dado de alta.");
					break;
				}
			}
			boolean seHaAgregadoOModificado = false;
			if(alta != null) {	//Si existe ya dado de alta.
				//Solo corroboro si tiene todos los módulos dados de alta
				seHaAgregadoOModificado = alta.agregarTodosLosModulosTipoOrdenadoYNoRepetido();
			}
			else {//No existe en BD, doy de alta
				Role rol = roleRepository.findByRoleName(roleName).orElseThrow(
						() -> new ResourceNotFoundException("Role", "role_name", roleName.toString()));
				alta = new ModuloVisibilidadPorRol(rol);
				seHaAgregadoOModificado = true;
			}
			if(seHaAgregadoOModificado)
				dadosDeAlta.add(moduloVisibilidadPorRolRepository.save(alta).toPayload());
		}
		LOGGER.info("Todos los módulos han sido dados de alta. Total de " + dadosDeAlta.size() + " módulos dados de alta.");
		return dadosDeAlta;
	}
	
	public void suscripcionCompleta() {
		List<ModuloVisibilidadPorRol> modulos = moduloVisibilidadPorRolRepository.findAll();
		for(ModuloVisibilidadPorRol m: modulos) {
			m.suscribir();
		}
		moduloVisibilidadPorRolRepository.saveAll(modulos);
	}
	
	public void desuscripcionCompleta() {
		List<ModuloVisibilidadPorRol> modulos = moduloVisibilidadPorRolRepository.findAll();
		for(ModuloVisibilidadPorRol m: modulos) {
			m.desuscribir();
		}
		moduloVisibilidadPorRolRepository.saveAll(modulos);
	}
	
	public void suscripcion(ModuloEnum moduloEnum) {
		if(moduloEnum.equals(ModuloEnum.MARKETPLACE))
			throw new BadRequestException("No se puede suscribir a MarketPlace");
		if(moduloEnum.isFreeModule())
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");
		List<ModuloVisibilidadPorRol> modulos = moduloVisibilidadPorRolRepository.findAll();
		modulos.forEach(m -> m.suscribir(moduloEnum));
		moduloVisibilidadPorRolRepository.saveAll(modulos);
	}
	
	public void desuscripcion(ModuloEnum moduloEnum) {
		if(moduloEnum.equals(ModuloEnum.MARKETPLACE))
			throw new BadRequestException("No se puede desuscribir a MarketPlace");
		if(moduloEnum.isFreeModule())
			throw new BadRequestException("No se puede desuscribir a un módulo gratuito");
		List<ModuloVisibilidadPorRol> modulos = moduloVisibilidadPorRolRepository.findAll();
		modulos.forEach(m -> m.desuscribir(moduloEnum));
		moduloVisibilidadPorRolRepository.saveAll(modulos);
	}
	
	public void desuscripcion(RoleName roleName) {
		ModuloVisibilidadPorRol modulo = moduloVisibilidadPorRolRepository.findByRoleRoleName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "Role->RoleName", roleName.toString()));
		modulo.desuscribir();
		moduloVisibilidadPorRolRepository.save(modulo);
	}
	
	public ModuloPayload modificarModuloVisibilidadTipos(ModuloPayload moduloNuevo) {
		RoleName roleName = RoleName.valueOf(moduloNuevo.getRol());
		//Role rol = roleRepository.findByRoleName(roleName).orElseThrow(
		//		() -> new ResourceNotFoundException("Role", "role_name", roleName.toString()));
		ModuloVisibilidadPorRol moduloBD = moduloVisibilidadPorRolRepository.findByRoleRoleName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "Role->RoleName", roleName.toString()));
		boolean huboModificaciones = false;	//inicializo false, si no hay modificaciones, no hago save para no desperdiciar tiempo.
		for(ModuloItemPayload pNew: moduloNuevo.getItems()) {	//Recorro módulos a modificar
			ModuloEnum pNewEnum = ModuloEnum.valueOf(pNew.getModuloEnum());
			for(ModuloVisibilidadPorRolTipo mBD: moduloBD.getModulos()) {	//Recorro módulos existentes
				if(pNewEnum.equals(mBD.getModuloEnum())) {					//Encontré el modulo a modificar? Si/No
					ModuloTipoVisibilidadEnum pNewVisibilidad = ModuloTipoVisibilidadEnum.valueOf(pNew.getTipoVisibilidad());
					if(mBD.setTipoVisibilidad(pNewVisibilidad)) {			//Modifico, si hubo modificación hago save
						huboModificaciones = true;					//Hago save si encontré, sino no desperdicio tiempo save a BD.
					}
				}
			}
		}
		if(huboModificaciones)
			return moduloVisibilidadPorRolRepository.save(moduloBD).toPayload();
		else
			return null;
	}
	
	private ModuloVisibilidadPorRol modificarModuloVisibilidadTipos(ModuloVisibilidadPorRol moduloNuevo) {
		//ModuloVisibilidadPorRol moduloBD = moduloVisibilidadPorRolRepository.findByRole(moduloNuevo.getRole()).orElseThrow(
		//		() -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "Role->RoleName", moduloNuevo.getRole().toString()));
		Optional<ModuloVisibilidadPorRol> optionalModuloBD = moduloVisibilidadPorRolRepository.findByRole(moduloNuevo.getRole());
		if(!optionalModuloBD.isPresent()) {
			this.agregarTodosLosModulos();
		}
		ModuloVisibilidadPorRol moduloBD = optionalModuloBD.get();
		boolean huboModificaciones = false;	//inicializo false, si no hay modificaciones, no hago save para no desperdiciar tiempo.
		for(ModuloVisibilidadPorRolTipo mNew: moduloNuevo.getModulos()) {	//Recorro módulos a modificar
			for(ModuloVisibilidadPorRolTipo mBD: moduloBD.getModulos()) {	//Recorro módulos existentes
				if(mNew.getModuloEnum().equals(mBD.getModuloEnum())) {		//Encontré el modulo a modificar? Si/No
					boolean poseeSuscripcion = moduloMarketService.poseeSuscripcionActiva(mNew.getModuloEnum());//True implica que tambien sea premium
					boolean esModuloPremium = mBD.getModuloEnum().isPaidModule();
					ModuloTipoVisibilidadEnum newVisibilidad = mNew.getTipoVisibilidad();
					if(poseeSuscripcion && newVisibilidad.equals(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION))
						mNew.setTipoVisibilidad(ModuloTipoVisibilidadEnum.NO_VISTA);
					//poseeSuscripcion devuelve false a un modulo free.
					else if(esModuloPremium && !poseeSuscripcion && !newVisibilidad.equals(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION))
						mNew.setTipoVisibilidad(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION);
					//Modifico, si hubo modificación hago save
					boolean seModificoModuloTipo = mBD.setTipoVisibilidad(mNew.getTipoVisibilidad());
					if(seModificoModuloTipo) {	
						huboModificaciones = true;							//Hago save si encontré, sino no desperdicio tiempo save a BD.
					}
				}
			}
		}
		if(huboModificaciones)
			return moduloVisibilidadPorRolRepository.save(moduloBD);
		else
			return null;
	}
	
	/**
	 * Requiere roles cargados.
	 */
	public void cargarVisibilidadSuscripcionDefault() {
		agregarTodosLosModulos();
		suscripcionCompleta();
		cargarTodosLosModulosConSuscripcionDefault();
	}
	
	private void cargarTodosLosModulosConSuscripcionDefault() {
		List<Role> roles = roleRepository.findAll();
		for(Role role: roles) {
			ModuloVisibilidadPorRol modulo = new ModuloVisibilidadPorRol();
			modulo.setRole(role);
			switch(role.getRoleName()) {
			case ROLE_ADMIN:
				//List<ModuloVisibilidadPorRolTipo> test = obtenerModuloVisibilidadRolAdmin();
				//System.out.println("Test");
				//modulo.setModulos(test);
				modulo.setModulos(obtenerModuloVisibilidadRolAdmin());
				break;
			case ROLE_EMPLOYEE:
				modulo.setModulos(obtenerModuloVisibilidadRolEmployee());
				break;
			case ROLE_PROFESIONAL:
				modulo.setModulos(obtenerModuloVisibilidadRolProfesional());
				break;
			case ROLE_USER:
				modulo.setModulos(obtenerModuloVisibilidadRolUser());
				break;
			case ROLE_DEFAULT:
				modulo.setModulos(obtenerModuloVisibilidadRolDefault());
				break;
			default:
				break;
				
			}
			modificarModuloVisibilidadTipos(modulo);
		}
	}
	
	private List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolDefault() {
		List<ModuloVisibilidadPorRolTipo> moduloTipos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		return moduloTipos;
	}
	
	private List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolUser() {
		List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolDefault();
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.forEach(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.SOLO_VISTA));
		return moduloTipos;
	}
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolProfesional() {
		List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolUser();
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.USERS, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR));
		//Total 3+14 = 17 modulos
		return moduloTipos;
	}	
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolEmployee() {
		List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolProfesional();
		//Stream map no almacena al realizar set.
		//moduloTipos.stream().map(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.INSUMO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.forEach(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		//Total 3+14+1 = 18 modulos
		return moduloTipos;
	}
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolAdmin() {
		List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolEmployee();
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.MARKETPLACE, ModuloTipoVisibilidadEnum.EDITAR));
		//Stream map no almacena al realizar set.
		//moduloTipos.stream().map(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.forEach(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		//Total 3+14+1+1 = 19 modulos
		return moduloTipos;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Basura de código. Esto era cuando se permitía dar de baja.
	
	/*public ModuloPayload altaModuloVisibilidadPorRol(ModuloPayload moduloPayload) {
		RoleName roleName = RoleName.valueOf(moduloPayload.getRol());
		Role rol = roleRepository.findByRoleName(roleName).orElseThrow(
				() -> new ResourceNotFoundException("Role", "role_name", moduloPayload.getRol()));
		
		//Verifico que no esté de alta el módulo con mismo RoleName antes del alta
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleRoleName(roleName);
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
	}*/
	
	/**
	 * No debería usarse
	 * @param id de módulo
	 * @return true si se realizó, false si hubo error
	 */
	/*public boolean bajaModuloVisibilidadPorRol(Long id) {
		try {
			ModuloVisibilidadPorRol rolBaja = moduloVisibilidadPorRolRepository.findById(id).orElseThrow(
	                () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "id", id));
			List<ModuloVisibilidadPorRolTipo> modulosTipo = rolBaja.getModulos();
			rolBaja.setModulos(null);
			moduloVisibilidadPorRolRepository.save(rolBaja);	//quito sus modulo tipo de bd
			for(ModuloVisibilidadPorRolTipo moduloTipo: modulosTipo)	//y ahora los doy de baja de la bd, ya que no dependen de nada
				moduloVisibilidadPorRolTipoRepository.deleteById(moduloTipo.getId());
			//Una vez eliminado sus módulos tipo, elimino el módulo general.
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
		
	}*/
	
	/*public void darTodaVisibilidadAdmin() {
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleRoleName(RoleName.ROLE_ADMIN);
		
		if(!optional.isPresent()) {   //Si no existe
			throw new BadRequestException("No existe visibilidad de módulo de admin en BD");
		
		ModuloVisibilidadPorRol modulo = optional.get();
		modulo
		for(ModuloVisibilidadPorRol modulo: moduloVisibilidadPorRols) {
			boolean deboGuardar = modulo.quitarModulo(moduloEnum);
			if(deboGuardar)
				moduloVisibilidadPorRolRepository.save(modulo);
		}
		
	}*/
	
	//public ModuloVisibilidadPorRol modificarModuloVisibilidadPorRol(ModuloPayload payload) {
	//	ModuloVisibilidadPorRol model = new ModuloVisibilidadPorRol(payload);
	//	if (moduloVisibilidadPorRol != null && moduloVisibilidadPorRol.getId() != null)
	//		return moduloVisibilidadPorRolRepository.save(moduloVisibilidadPorRol);
	//	else
	//		return null;
	//}
	
}
