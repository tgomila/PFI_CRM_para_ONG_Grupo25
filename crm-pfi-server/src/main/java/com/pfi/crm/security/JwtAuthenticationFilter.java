package com.pfi.crm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pfi.crm.constant.JWTConstants;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.mastertenant.entity.MasterTenant;
import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.util.JwtTokenProviderUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/*
 * Este filtro:
 * 		- Lee los JWT authentication de Authentication header de todos los requests.
 * 		- Valida el token.
 * 		- carga usuario asociado al token.
 * 		- Configura el user details en Spring security SecurityContext.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProviderUtil tokenProvider;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private MasterTenantService masterTenantService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		String header = request.getHeader(JWTConstants.HEADER_STRING);
		String username = null;
		String audience = null; // tenantOrClientId
		String authToken = null;
		if (header != null && header.startsWith(JWTConstants.TOKEN_PREFIX)) {
			authToken = header.replace(JWTConstants.TOKEN_PREFIX, "");
			try {
				username = tokenProvider.getUsernameFromToken(authToken);
				audience = tokenProvider.getAudienceFromToken(authToken);
				MasterTenant masterTenant = masterTenantService.findByClientId(Integer.valueOf(audience));
				if (null == masterTenant) {
					logger.error("An error during getting tenant name");
					throw new BadCredentialsException("Invalid tenant and user.");
				}
				DBContextHolder.setCurrentDb(masterTenant.getDbName());
			} catch (IllegalArgumentException ex) {
				logger.error("An error during getting username from token", ex);
			} catch (ExpiredJwtException ex) {
				logger.warn("The token is expired and not valid anymore", ex);
			} catch (SignatureException ex) {
				logger.error("Authentication Failed. Username or Password not valid.", ex);
			}
		} else {
			logger.warn("Couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
			if (tokenProvider.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
		
		/*try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				Long userId = tokenProvider.getUserIdFromJWT(jwt);

				//
				// * Si lo deseo, podría pedir el username en vez del id. Podría ahorrarme una
				// * consulta a la BD. El problema es que si se editan los roles del usuario o se
				// * cambia algo en la BD, es necesario consultar a la BD.
				//
				UserDetails userDetails = customUserDetailsService.loadUserById(userId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);*/
	}

	/*private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}*/
}
