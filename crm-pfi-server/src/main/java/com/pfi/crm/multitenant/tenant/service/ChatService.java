package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Chat;
import com.pfi.crm.multitenant.tenant.payload.ChatPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ChatRepository;

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
	
	public ChatPayload altaChat (ChatPayload payload) {
		payload.setId(null);
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
