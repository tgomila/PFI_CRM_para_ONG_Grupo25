package com.pfi.crm.security;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	//Anteriormente se llamaba customUserDetailsService, porque es mi usuario custom
    @Autowired
    UserRepository userRepository;
    
    /*
     * Genera una clase de "User" que entiende Spring, transforma nuestro User.java en UserDetail.java
     * Este truco permite que cualquier .java sea un usuario, y transformarlo en UserDetail.
     */
    
    @Override
    @Transactional("tenantTransactionManager")
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );

        return UserPrincipal.build(user);
    }

    @Transactional("tenantTransactionManager")
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.build(user);
    }
}