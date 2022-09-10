package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	Optional<Chat> findByFromUsername(String username);
	
	Optional<Chat> findByToUsername(String username);
}
