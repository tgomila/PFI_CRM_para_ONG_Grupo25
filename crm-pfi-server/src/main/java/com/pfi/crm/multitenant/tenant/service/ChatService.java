package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Chat;
import com.pfi.crm.multitenant.tenant.payload.ChatPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ChatRepository;
import com.pfi.crm.security.UserPrincipal;

@Service
public class ChatService {
	
	@Autowired
	private ChatRepository chatRepository;
	
	//TODO
	public ChatPayload getChatByIdContacto(@PathVariable Long id) {
        return chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat", "id", id)).toPayload();
    }
	
	public List<ChatPayload> getChats() {
		return chatRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	/**
	 * Controlador debería tener como input "@CurrentUser UserPrincipal currentUser" para este método.
	 * @param payload
	 * @return
	 */
	public ChatPayload altaChat (UserPrincipal currentUser, ChatPayload payload) {
		if(payload==null)
			throw new BadRequestException("Ha intentado dar de alta un chat null!");
		if(payload.getUserNameTo() == null || payload.getUserNameTo().isEmpty())
			throw new BadRequestException("No ha ingresado a quién le envía el mensaje!");
		if(payload.getMensaje() == null || payload.getMensaje().isEmpty())
			throw new BadRequestException("No ha ingresado el mensaje!");
		payload.setId(null);
		payload.setUserNameFrom(currentUser.getUsername());
		return chatRepository.save(new Chat(payload)).toPayload();
	}
	
	public List<ChatPayload> getChatsByUsernameFrom(String usernameFrom) {
		return chatRepository.findByUserNameFrom(usernameFrom).stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	/*public Chat toModel(ChatPayload p) {
		Chat m = new Chat();
		m.setId(p.getId());
		m.setUserNameFrom(p.getUserNameFrom());
		m.setUserNameTo(p.getUserNameTo());
		m.setMensaje(p.getMensaje());
		m.setLeido(p.getLeido());
		return m;
	}
	
	public ChatPayload toPayload(Chat m) {
		ChatPayload p = new ChatPayload();
		p.setId(m.getId());
		p.setUserNameFrom(m.getUserNameFrom());
		p.setUserNameTo(m.getUserNameTo());
		p.setMensaje(m.getMensaje());
		p.setLeido(m.getLeido());
		return p;
	}*/
}
