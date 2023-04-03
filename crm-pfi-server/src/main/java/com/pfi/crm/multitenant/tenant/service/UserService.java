package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.AppException;
import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	public User getUserById(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
    }
	
	public User getUserByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }
	
	public List<User> getUsers() {
		return userRepository.findAll();
		//return userRepository.findAll().filter(a -> a.isActive()).collect(Collectors.toList());
    }
	
	public User altaUsuario (User user) {
		user.setId(null);//Para no modificar nada
		//Si hay contacto precargado, lo asocio sin modificar contacto. Si no hay id, lo creo
		if(user.getContacto() != null && user.getContacto().getId() != null) {
			Contacto contactoAsociar = contactoService.getContactoModelById(user.getContacto().getId());
			user.setContacto(contactoAsociar);
		}
		//Me aseguro su ID bien asociado. 
		Set<Role> rolesDBAgregar = new HashSet<Role>();
		user.getRoles().forEach((role) -> rolesDBAgregar.add(buscarRole(role.getRoleName())));
		user.setRoles(rolesDBAgregar);
		return userRepository.save(user);
	}
	
	public User agregarRol(String username, RoleName roleName) {
		User user = getUserByUsername(username);
		Role newUserRole = buscarRole(roleName);
		user.agregarRol(newUserRole);
		return userRepository.save(user);
	}
	
	private Role buscarRole(RoleName roleName) {
		return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", roleName.toString()));
	}
	
	public User quitarRol(String username, RoleName roleName) {
		User user = getUserByUsername(username);
		Role newUserRole = buscarRole(roleName);
		user.quitarRol(newUserRole);
		return userRepository.save(user);
	}
	
	public String bajaUsuariosPorContacto(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a dar de baja, por favor ingrese un número válido.");
		List<User> usuariosAModificar = userRepository.findByContacto_Id(id);
		if(!usuariosAModificar.isEmpty()) {
			
			List<String> usernames = new ArrayList<String>();
			usuariosAModificar.forEach((user) -> {
				if(user.getUsername() != null)
					usernames.add(user.getUsername());
				user.setContacto(null);
			});
			usuariosAModificar = userRepository.saveAll(usuariosAModificar);
			userRepository.deleteAll(usuariosAModificar);
			
			String message = "Se ha dado de baja ";
			if(usernames.size() > 1) {//Solo para que "usuarioS suene prural.
				message += "a usuarios con UserName: ";
				message += usernames.get(0);
				usernames.remove(0);
				for(String u: usernames)
					message += ", "+u;
			}
			else if(usernames.size() == 1) {
				message += "al usuario con UserName: " + usernames.get(0);
			}
			else {//size == 0
				message = "";//No deberia pasar de no encontrar usernames
			}
			if(!message.isEmpty())
				message += " cuyo Contacto ID es: " + id.toString();
			return message;
		}
		else {
			throw new AppException("No existen usuarios con Contacto ID: " + id + " para dar de baja.");
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return True si existe y se dió de baja, false si no existe y no se dió de baja.
	 */
	public String bajaUsuariosPorContactoSiExiste(Long id) {
		if(existeUserPorIdContacto(id))
			return bajaUsuariosPorContacto(id);
		return "";
	}
	
	public String desasociarContactoDeUsers(Long id) {
		if(id == null)
			throw new BadRequestException("Ha introducido un id='null' a desasociar, por favor ingrese un número válido.");
		List<User> usuariosAModificar = userRepository.findByContacto_Id(id);
		if(!usuariosAModificar.isEmpty()) {
			
			List<String> usernames = new ArrayList<String>();
			usuariosAModificar.forEach((user) -> {
				if(user.getUsername() != null)
					usernames.add(user.getUsername());
				user.setContacto(null);
			});
			usuariosAModificar = userRepository.saveAll(usuariosAModificar);
			
			String message = "Se ha desasociado ";
			if(usernames.size() > 1) {//Solo para que "usuarioS suene prural.
				message += "a usuarios con UserName: ";
				message += usernames.get(0);
				usernames.remove(0);
				for(String u: usernames)
					message += ", "+u;
			}
			else if(usernames.size() == 1) {
				message += "al usuario con UserName: " + usernames.get(0);
			}
			else {//size == 0
				message = "";//No deberia pasar de no encontrar usernames
			}
			if(!message.isEmpty())
				message += " del Contacto ID: " + id.toString();
			return message;
		}
		return "";
		
	}
	
	public boolean existeUserPorIdContacto(Long id) {
		return userRepository.existsByContacto_Id(id);
	}
	
	
	
	/*public void bajaUser(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<User> optionalModel = userRepository.findById(id);
		if(optionalModel.isPresent()) {
			User m = optionalModel.get();
			m.setEstadoActivoUser(false);
			m.setFechaBajaUser(LocalDate.now());
			userRepository.save(m);
			//userRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe user
		}
		
	}
	
	public User modificarUser(User user) {
		if (user != null && user.getId() != null)
			return userRepository.save(user);
		else
			return new User();
	}
	
	
	
	
	
	
	
	
	// Payloads
	public User toModel(UserPayload p) {

		// User
		User m = new User();
		m.setId(p.getId());
		m.setEstadoActivoUser(true);
		m.setFechaAltaUser(LocalDate.now());
		m.setFechaBajaUser(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());

		return m;
	}

	public UserPayload toPayload(User m) {

		UserPayload p = new UserPayload();

		// User
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		// Fin User

		return p;
	}*/
}
