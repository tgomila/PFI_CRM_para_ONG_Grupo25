package com.pfi.crm.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.UserPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.UserService;
import com.pfi.crm.payload.request.SignUpRequest;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	@GetMapping("/{id}")
	public UserPayload getUserById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.USERS, "Ver user id: '" + id + "'");
		return userService.getUserById(id);
	}
	
	@GetMapping({"/", "/all"})
	public List<UserPayload> getUser(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.USERS, "Ver users");
		return  userService.getUsers();
	}
	
	@PostMapping({"/", "/alta"})
	public UserPayload altaUsuario(@Valid @RequestBody SignUpRequest payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.USERS, "Cargar un nuevo usuario");
		return userService.altaUser(payload);
	}
	
	@DeleteMapping({"/{id}", "/baja/{id}"})
	public ResponseEntity<?> bajaUser(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.USERS, "Dar de baja a user con id: '" + id + "'");
		String message = userService.bajaUser(id);
		if(!message.isEmpty())
			return ResponseEntity.ok().body(new ApiResponse(true, message));
		else
			throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}
	
	@PutMapping({"/", "/modificar"})
	public UserPayload modificarUser(@Valid @RequestBody UserPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.USERS, "Modificar user");
		return userService.modificarUser(payload);
	}
	
	@GetMapping("/list")
	public List<Map<String, Object>> getUsersWithContacto(@CurrentUser UserPrincipal currentUser) {
		System.out.println("Llegue aqui principio");
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.USERS, "Ver users");
		return userService.getUsersWithContacto();
	}
	
	/*@PostMapping({"/", "/alta"})
	public UserPayload altaUser(@Valid @RequestBody UserPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.USERS, "Cargar nuevo user");
		return userService.altaUsuario(payload);
	}*/
}
