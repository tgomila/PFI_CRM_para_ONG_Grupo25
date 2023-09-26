package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.AppException;
import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.UserPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;
import com.pfi.crm.payload.request.SignUpRequest;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ContactoService contactoService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserPayload getUserById(@PathVariable Long id) {
		return this.getUserByIdModel(id).toPayload();
	}
	
	private User getUserByIdModel(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", id));
	}
	
	public User getUserByUsername(@PathVariable String username) {
		return userRepository.findByUsername(username).orElseThrow(
				() -> new ResourceNotFoundException("User", "username", username));
	}
	
	public List<UserPayload> getUsers() {
		//return userRepository.findAll().filter(a -> a.isActive()).collect(Collectors.toList());
		return userRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
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
		return "No existen usuarios con id: '" + id + "', por lo tanto se lo considera usuarios dados de baja";
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
			message += ". Si desea darlo de baja, recuerde los id's mencionados anteriormente,"
					+ " y délos de baja de forma aparte pidiendolo al Jefe/Administrador.";
			return message;
		}
		return "";
		
	}
	
	public boolean existeUserPorIdContacto(Long id) {
		return userRepository.existsByContacto_Id(id);
	}
	
	public List<Map<String, Object>> getUsersWithContacto() {
		return userRepository.getUsersWithContacto();
		//return userRepository.getUsersWithContactoAndPersona();
	}
	
	public String bajaUser(Long id) {
		User m = userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("User", "id", id));
		String message = "Se ha dado de baja al usuario id: " + m.getId();
		if(m.getContacto() != null) {
			message += ", y desasociado a su contacto id: " + m.getContacto().getId();
			m.setContacto(null);
			userRepository.save(m);
		}
		userRepository.delete(m);
		return message;
	}
	
	public UserPayload altaUser(SignUpRequest signUpRequest) {
		if (null == signUpRequest.getUsername() || signUpRequest.getUsername().isEmpty()) {
			throw new BadRequestException("Username is required.");
		}
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new BadRequestException("Username is already taken!");
		}

		if (null == signUpRequest.getEmail() || signUpRequest.getEmail().isEmpty()) {
			throw new BadRequestException("Email is required");
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email Address already in use!");
		}
		
		User userModel = new User(signUpRequest.getName(), signUpRequest.getUsername(),
				signUpRequest.getEmail(), signUpRequest.getPassword());

		userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
		
		//Asignar contacto
		if(signUpRequest.getContacto() != null && signUpRequest.getContacto().getId() != null) {
			Contacto contactoAsociar = contactoService.getContactoModelById(signUpRequest.getContacto().getId());
			userModel.setContacto(contactoAsociar);
		}
		//Asignar roles
		Set<RoleName> rolesPayload = signUpRequest.getRoles();
		Set<Role> newRoles = rolesPayload.stream()
				.map(this::buscarRole)
				.collect(Collectors.toSet());
		userModel.setRoles(newRoles);
		
		if(userModel.getRoles() != null && userModel.getRoles().isEmpty()) {
			Role roleDefault = buscarRole(RoleName.ROLE_USER);
			userModel.agregarRol(roleDefault);
		}
		

		return userRepository.save(userModel).toPayload();
	}
	
	public UserPayload modificarUser(UserPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null, no se realizará ninguna acción.");
		if(payload.getId() == null)
			throw new BadRequestException("Ha introducido un id null, no se realizará una modificación.");
		User userModel = this.getUserByIdModel(payload.getId());
		userModel.modificar(payload);//Modificar datos
		//Asignar contacto
		if(userModel.hayQueModificarSuContacto(payload)) {
			Contacto contactoAsociar = contactoService.getContactoModelById(payload.getContacto().getId());
			userModel.setContacto(contactoAsociar);
		}
		//Asignar roles
		if(userModel.hayQueModificarSusRoles(payload)) {
			Set<RoleName> rolesPayload = payload.getRoles();
			Set<Role> newRoles = rolesPayload.stream()
					.map(this::buscarRole)
					.collect(Collectors.toSet());
			userModel.setRoles(newRoles);
		}
		if(userModel.getRoles() != null && userModel.getRoles().isEmpty()) {
			Role roleDefault = buscarRole(RoleName.ROLE_DEFAULT);
			userModel.agregarRol(roleDefault);
		}
		userModel = userRepository.save(userModel);
		return userModel.toPayload();
	}
	
}
