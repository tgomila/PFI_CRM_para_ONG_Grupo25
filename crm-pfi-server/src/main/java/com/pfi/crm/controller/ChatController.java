package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.payload.ChatPayload;
import com.pfi.crm.multitenant.tenant.service.ChatService;



@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	
	
	@GetMapping("/{id}")
    public ChatPayload getChatById(@PathVariable Long id) {
        return chatService.getChatByIdContacto(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ChatPayload> getChat() {
    	return  chatService.getChats();
	}
	
	@GetMapping({"/{usernameFrom}"})
    public List<ChatPayload> getChatByUsernameFrom(@PathVariable String usernameFrom) {
    	return  chatService.getChatsByUsernameFrom(usernameFrom);
	}
	
	@PostMapping({"/", "/alta"})
    public ChatPayload altaChat(@Valid @RequestBody ChatPayload payload) {
    	return chatService.altaChat(payload);
    }
	
	
	
	@GetMapping("/nombredeheadersdetabla")
	public Object getNombreDeTabla() {
		return new ChatPayload().nombreDeColumnaParaTablaFrontend();
	}
	
	//TEST
	//Devuelve un ejemplo de Chat
	
	@GetMapping("/test")
	public ChatPayload testChat(/* @Valid @RequestBody ChatPayload payload */) {
		System.out.println("Entre aca");
		
		ChatPayload p = new ChatPayload();
		p.setId(null);
		p.setUserNameFrom("user");
		p.setUserNameTo("admin");
		p.setMensaje("Hola mundo!");
		p.setLeido(false);
				
		return this.altaChat(p);
	}
}
