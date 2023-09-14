package com.pfi.crm.controller;

import com.pfi.crm.constant.Estado;
import com.pfi.crm.exception.AppException;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.mastertenant.entity.MasterTenant;
import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.model.User;
import com.pfi.crm.multitenant.tenant.payload.TenantPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.UserRepository;
import com.pfi.crm.payload.request.LoginRequest;
import com.pfi.crm.payload.request.SignUpRequest;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.payload.response.JwtAuthenticationResponse;
import com.pfi.crm.security.UserTenantInformation;
import com.pfi.crm.util.JwtTokenProviderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements Serializable {
	
	private static final long serialVersionUID = 2932004521147626605L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProviderUtil tokenProvider;

	// Codigo Tenant
	@Autowired
	private MasterTenantService masterTenantService;

	private Map<String, String> mapValue = new HashMap<>();
	private Map<String, String> userDbMap = new HashMap<>();

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws AuthenticationException {
		
		//Chequear login
		LOGGER.info("userLogin() method call...");
		if(null == loginRequest.getUsernameOrEmail() || loginRequest.getUsernameOrEmail().isEmpty()){
			return new ResponseEntity<>("Usuario o email es requerido", HttpStatus.BAD_REQUEST);
		}
		
		if(null == loginRequest.getPassword() || loginRequest.getPassword().isEmpty()){
			return new ResponseEntity<>("La contraseña es requerida", HttpStatus.BAD_REQUEST);
		}
		
		if(null == loginRequest.getTenantOrClientId()){
			return new ResponseEntity<>("ID Tenant es requerido para la conexión", HttpStatus.BAD_REQUEST);
		}
		//Fin chequear login
		
		//Setear base de datos o schema
		MasterTenant masterTenant = masterTenantService.findByClientId(loginRequest.getTenantOrClientId());
		if(null == masterTenant || masterTenant.getStatus().toUpperCase().equals(Estado.Inactivo.toString())){
			throw new RuntimeException("Por favor contacte a soporte.");
		}
		DBContextHolder.setCurrentDb(masterTenant.getDbName());
		//Entry Client Wise value dbName store into bean.
		String userName = loginRequest.getUsernameOrEmail();
		if(loginRequest.getUsernameOrEmail().contains("@")) {
			User user = userRepository.findByEmail(loginRequest.getUsernameOrEmail()).get();
			if(user!=null)
				userName = user.getEmail();
		}
		loadCurrentDatabaseInstance(masterTenant.getDbName(), userName /*loginRequest.getUsernameOrEmail()*/);
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(), 
						loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		System.out.println("userDetails: " + userDetails.getUsername() + " psw: " + userDetails.getPassword() + " auth: " + userDetails.getAuthorities().toString());
		final String token = tokenProvider.generateToken(userDetails.getUsername(),String.valueOf(loginRequest.getTenantOrClientId()));
		//Map the value into applicationScope bean
		setMetaDataAfterLogin();
		TenantPayload tenantPayload = masterTenant.toPayload();
		return ResponseEntity.ok(new JwtAuthenticationResponse(
				userDetails.getUsername(),
				token,
				roles,
				tenantPayload.getTenantClientId(),
				tenantPayload.getDbName(),
				tenantPayload.getTenantName()));
		
		/*Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsernameOrEmail(),
						loginRequest.getPassword()
				)
		);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));*/
	}
	
	private void loadCurrentDatabaseInstance(String databaseName, String userName) {
		DBContextHolder.setCurrentDb(databaseName);
		mapValue.put(userName, databaseName);
	}
	
	@Bean(name = "userTenantInfo")
	@ApplicationScope
	public UserTenantInformation setMetaDataAfterLogin() {
		UserTenantInformation tenantInformation = new UserTenantInformation();
		if (mapValue.size() > 0) {
			for (String key : mapValue.keySet()) {
				if (userDbMap.get(key) == null) {
					// Here Assign putAll due to all time one come.
					userDbMap.putAll(mapValue);
				} else {
					userDbMap.put(key, mapValue.get(key));
				}
			}
			mapValue = new HashMap<>();
		}
		tenantInformation.setMap(userDbMap);
		return tenantInformation;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody @NotNull SignUpRequest signUpRequest)
			throws AuthenticationException {
		// Setear base de datos o schema
		if (null == signUpRequest.getTenantOrClientId()) {
			return new ResponseEntity<>("Tenant id is required",
					HttpStatus.BAD_REQUEST);
		}

		if (null == signUpRequest.getUsername() || signUpRequest.getUsername().isEmpty()) {
			return new ResponseEntity<>("Username is required",
					HttpStatus.BAD_REQUEST);
		}

		MasterTenant masterTenant = masterTenantService.findByClientId(signUpRequest.getTenantOrClientId());
		if (null == masterTenant || masterTenant.getStatus().toUpperCase().equals(Estado.Inactivo.toString())) {
			throw new RuntimeException("Por favor contacte a soporte.");
		}
		DBContextHolder.setCurrentDb(masterTenant.getDbName());

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>("Username is already taken!",
					HttpStatus.BAD_REQUEST);
		}

		if (null == signUpRequest.getEmail() || signUpRequest.getEmail().isEmpty()) {
			return new ResponseEntity<>("Email is required",
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>("Email Address already in use!",
					HttpStatus.BAD_REQUEST);
		}
		
		//Entry Client Wise value dbName store into bean.
		loadCurrentDatabaseInstance(masterTenant.getDbName(), signUpRequest.getUsername());
		
		// Creacion del usuario
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
				signUpRequest.getEmail(), signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
