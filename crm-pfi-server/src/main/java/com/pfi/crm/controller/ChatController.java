package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ChatPayload;
import com.pfi.crm.multitenant.tenant.service.ChatService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;



@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	//@GetMapping("/{id}")
    //public ChatPayload getChatById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
	//	seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "Buscar un mensaje de chat por su ID");
	//	return chatService.getChatByIdContacto(id);
	//}
	
	@GetMapping({"/", "/all"})
    public List<ChatPayload> getChat(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CHAT, "Ver todos los chats");
    	return  chatService.getChats();
	}
	
	@GetMapping({"/from"})
    public List<ChatPayload> getChatByUsernameFrom(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "");
		return chatService.getChatsByUsernameFrom(currentUser.getUsername());
	}
	
	@PostMapping({"/", "/alta"})
    public ChatPayload altaChat(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody ChatPayload payload) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CHAT, "");
		return chatService.altaChat(currentUser, payload);
    }
	
	
	
	@GetMapping("/nombredeheadersdetabla")
	public Object getNombreDeTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "");
		return new ChatPayload().nombreDeColumnaParaTablaFrontend();
	}
	
	//TEST
	//Devuelve un ejemplo de Chat
	
	@GetMapping("/test")
	public ChatPayload testChat(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CHAT, "");
		System.out.println("Entre aca");
		
		ChatPayload p = new ChatPayload();
		p.setId(null);
		p.setUserNameFrom("user");
		p.setUserNameTo("admin");
		p.setMensaje("Hola mundo!");
		p.setLeido(false);
				
		return p;
	}
}
