package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	Optional<Chat> findByUserNameFrom(String userNameFrom);
	
	Optional<Chat> findByUserNameTo(String userNameTo);
}
