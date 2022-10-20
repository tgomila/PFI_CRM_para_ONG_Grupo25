package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	Optional<Chat> findByUserNameFrom(String userNameFrom);
	
	Optional<Chat> findByUserNameTo(String userNameTo);
}
