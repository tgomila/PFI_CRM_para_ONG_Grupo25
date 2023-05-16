package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ForbiddenException;
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
	
	/**
	 * Un método que chequea permisos del usuario. Si tiene permisos sigue de largo, caso contrario lanza un throw Forbidden Exception.<br>
	 * Evitaría roles bajos acceder a recursos no autorizados de roles superiores.<br><br>
	 * 
	 * Imagina que estas en un supermercado como cliente y entras a una puerta de "Solo empleados", podes pasar pero un patova (este método) te espera a chequear tus credenciales.<br>
	 * Este método es como el patova del boliche, entras al método pero si no tenes el permiso, te echa y no se ejecuta el método del controlador.<br>
	 * 	Si tenes el permiso, te deja pasar con gusto a todo lo que quieras.<br>
	 * Este es un método de seguridad, que suma seguridad.<br><br>
	 * 
	 * @param currentUser en tu controller pone (@CurrentUser UserPrincipal currentUser) para acceder al usuario.
	 * @param visibilidadRequerida por ejemplo si estas dentro de método getAll() pone "ModuloTipoVisibilidadEnum.SOLO_VISTA", si el User posee permisos SOLO_VISTA o EDITAR podría acceder.
	 * @param modulo_a_acceder por ejemplo si estas en ActividadController pone: "ModuloEnum.ACTIVIDAD".
	 * @param mensajeAccion por ejemplo si estas en ActividadController pone: "ver 1 id de actividad".
	 */
	public void poseePermisosParaAccederAlMetodo(UserPrincipal currentUser, ModuloTipoVisibilidadEnum visibilidadRequerida, ModuloEnum modulo_a_acceder, String mensajeAccion) {
		Set<String> rolesUsuario = new HashSet<>();//Esto es solo si no tiene permisos, se prepara el mensaje.
		for (GrantedAuthority authority : currentUser.getAuthorities()) {
			String roleString = authority.getAuthority();
			RoleName unRolDelUsuario = RoleName.valueOf(roleString);
			ModuloVisibilidadPorRol moduloVisibilidad = getModulosVisibilidadPorRolModel(unRolDelUsuario);
			if(moduloVisibilidad != null && moduloVisibilidad.poseePermiso(modulo_a_acceder, visibilidadRequerida))
				return;//Posee permisos, ya puede volver
			rolesUsuario.add(unRolDelUsuario.getName());
		}
		
		//Exception no tiene permisos, se preparará el mensaje:
		String accion = mensajeAccion != null && !mensajeAccion.isEmpty() ? mensajeAccion : visibilidadRequerida.getName();
		String modulo = modulo_a_acceder.getName();
		String mensaje = "Su usuario " + currentUser.getUsername();
		if(rolesUsuario.size()==0)
			mensaje += " no posee ningún rol asignado, por lo tanto no tiene permiso";
		else if(rolesUsuario.size()==1)
			mensaje += " con rol '" + rolesUsuario.iterator().next() + "' no tiene permiso";
		else if(rolesUsuario.size()>=2) {
			mensaje += " con roles:";
			for(String rol: rolesUsuario)
				mensaje += " '" + rol + "',";
			mensaje += " no tiene permisos";
		}
		mensaje+= " para realizar la acción de: '" + accion + "' en módulo: '" + modulo + "'.";
		throw new ForbiddenException(mensaje);
	}
	
	public List<ModuloItemPayload> getModulosVisibilidadPorRol(UserPrincipal currentUser){
		User user = obtenerUser(currentUser);
		RoleName rolSuperior = user.getRoleMasValuado();
		List<ModuloVisibilidadPorRolTipo> items = this.getModulosVisibilidadPorRolModel(rolSuperior).getModulos();
		
		if(user.getRoles().size()>=2) {//Situacion rara pero ante la duda se programa
			Set<RoleName> rolesUser = user.getRoleNames();
			rolesUser.remove(rolSuperior);
			//Si hay uno que es solo_vista y otro editar, me quedo con editar.
			for(RoleName rol: rolesUser) {
				List<ModuloVisibilidadPorRolTipo> itemsAux = this.getModulosVisibilidadPorRolModel(rol).getModulos();
				//recorro los permisos de este rol
				for(ModuloVisibilidadPorRolTipo itemMejorRango: items) {
					for(ModuloVisibilidadPorRolTipo itemComparar: itemsAux) {
						if(itemMejorRango.getModuloEnum().equals(itemComparar.getModuloEnum()) &&
								!itemMejorRango.getTipoVisibilidad().esMejorQue(itemComparar.getTipoVisibilidad())){
							itemMejorRango.setTipoVisibilidad(itemComparar.getTipoVisibilidad());
						}
					}
					
				}
			}
		}
		return items.stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	//Obsoleto
	//public ModuloPayload getModulosVisibilidadPorRol(UserPrincipal currentUser) {
	//	RoleName rolSuperior = obtenerMejorRoleDelUser(currentUser);
	//	
	//	return moduloVisibilidadPorRolRepository.findByRoleRoleName(rolSuperior).orElseThrow(
    //            () -> new ResourceNotFoundException("ModuloVisibilidadPorRol", "Role->RoleName", rolSuperior.toString())).toPayload();
	//}
	
	//private RoleName obtenerMejorRoleDelUser(UserPrincipal currentUser) {
	//	User user = obtenerUser(currentUser);
	//	RoleName rolSuperior = user.getRoleMasValuado();
	//	System.out.println("El rol superior es: " + rolSuperior.getName());
	//	return rolSuperior;
	//}
	
	private User obtenerUser(UserPrincipal currentUser) {
		if(currentUser == null || currentUser.getId() == null)//no deberia suceder
			throw new BadRequestException("Ha iniciado sesión con un User cuyo id es null");
		User user = userRepository.findById(currentUser.getId()).get();
		if(user == null)
			throw new BadRequestException("Ha iniciado sesión con un id que ya no existe o ha sido borrado, por favor cierre sesión en su navegador.");
		return user;
	}
	
	public ModuloPayload getModulosVisibilidadPorRol(RoleName roleName){
		return getModulosVisibilidadPorRolModel(roleName).toPayload();
    }
	
	private ModuloVisibilidadPorRol getModulosVisibilidadPorRolModel(RoleName roleName){
		Optional<ModuloVisibilidadPorRol> optional = moduloVisibilidadPorRolRepository.findByRoleRoleName(roleName);
		if(optional.isPresent()) {   //Si existe
			return optional.get();
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
		//List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolDefault();
		List<ModuloVisibilidadPorRolTipo> moduloTipos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		//moduloTipos.forEach(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.SOLO_VISTA));
		return moduloTipos;
	}
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolProfesional() {
		//List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolUser();
		List<ModuloVisibilidadPorRolTipo> moduloTipos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR));
		//Total 3+14 = 17 modulos
		return moduloTipos;
	}	
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolEmployee() {
		//List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolProfesional();
		List<ModuloVisibilidadPorRolTipo> moduloTipos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		
		//Personas
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.VOLUNTARIO, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.SOLO_VISTA));

		//Acciones de personas
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		
		//Chat
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR));
		//Stream map no almacena al realizar set.
		//moduloTipos.stream().map(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.forEach(m -> m.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR));
		//Total 3+14+1 = 18 modulos
		return moduloTipos;
	}
	
	public List<ModuloVisibilidadPorRolTipo> obtenerModuloVisibilidadRolAdmin() {
		//List<ModuloVisibilidadPorRolTipo> moduloTipos = obtenerModuloVisibilidadRolEmployee();
		List<ModuloVisibilidadPorRolTipo> moduloTipos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		
		//Personas
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CONTACTO, ModuloTipoVisibilidadEnum.NO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONA, ModuloTipoVisibilidadEnum.NO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.BENEFICIARIO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.VOLUNTARIO, ModuloTipoVisibilidadEnum.NO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.EMPLEADO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.COLABORADOR, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CONSEJOADHONOREM, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PERSONAJURIDICA, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROFESIONAL, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.USERS, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		
		//Acciones de personas
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.ACTIVIDAD, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROGRAMA_DE_ACTIVIDADES, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRODUCTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.DONACION, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.FACTURA, ModuloTipoVisibilidadEnum.EDITAR));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.INSUMO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PRESTAMO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.PROYECTO, ModuloTipoVisibilidadEnum.SOLO_VISTA));
		moduloTipos.add(new ModuloVisibilidadPorRolTipo(ModuloEnum.CHAT, ModuloTipoVisibilidadEnum.EDITAR));
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
