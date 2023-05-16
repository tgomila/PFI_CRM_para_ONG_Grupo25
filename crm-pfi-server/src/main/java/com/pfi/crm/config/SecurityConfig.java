package com.pfi.crm.config;

import com.pfi.crm.security.JwtUserDetailsService;
import com.pfi.crm.security.JwtAuthenticationEntryPoint;
import com.pfi.crm.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
 * extemds WebSecurityConfigurerAdapter:
 *    - Provee configuración de seguridad por defecto.
 *    - Permite customizar la seguridad de otras clases en sus metodos, al extender esta clase.
 */

@Configuration
@EnableWebSecurity				//Habilita el web security en el proyecto.
@EnableGlobalMethodSecurity(	//Habilita seguridad en metodos con annotations.
        securedEnabled = true,	//Habilita @Secured annotation, ejemplo: @Secured({"ROLE_USER", "ROLE_ADMIN"})
        jsr250Enabled = true,	//Habilita @RolesAllowed annotation, ejemplo: @RolesAllowed("ROLE_ADMIN").
        prePostEnabled = true	//habilita @PreAuthorize y @PostAuthorize annotations, ejemplo: @PreAuthorize("isAnonymous()")
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    // HttpSecurity se usa para sesiones en sessionManagement, agrega las reglas de ROLE
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors()
					.and()
				.csrf()
					.disable()
				.exceptionHandling()
					.authenticationEntryPoint(unauthorizedHandler)
					.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
				.authorizeRequests()
					.antMatchers("/",
						"/favicon.ico",
						"/**/*.png",
						"/**/*.gif",
						"/**/*.svg",
						"/**/*.jpg",//Esto permite consultar mis imágenes sin token
						"/**/*.html",
						"/**/*.css",
						"/**/*.js")
						.permitAll()
					.antMatchers("/api/auth/**")
					//.antMatchers("/api/**")
						.permitAll()
					.antMatchers("/api/tenant/all")
						.permitAll()
					.antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
						.permitAll()
					//.antMatchers(HttpMethod.GET, "/api/images/images/**")
					//	.permitAll()
					//.antMatchers(HttpMethod.GET, "/api/employee/**", "/api/users/**", "/api/contacto/**")
					//	.permitAll()
					.anyRequest()
						.authenticated();

		// Agrega nuestro custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
    }
    
    
	@SuppressWarnings("rawtypes")
	@Bean
	public FilterRegistrationBean platformCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configAutenticacao = new CorsConfiguration();
		configAutenticacao.setAllowCredentials(true);
		configAutenticacao.addAllowedOrigin("*");
		configAutenticacao.addAllowedHeader("Authorization");
		configAutenticacao.addAllowedHeader("Content-Type");
		configAutenticacao.addAllowedHeader("Accept");
		configAutenticacao.addAllowedMethod("POST");
		configAutenticacao.addAllowedMethod("GET");
		configAutenticacao.addAllowedMethod("DELETE");
		configAutenticacao.addAllowedMethod("PUT");
		configAutenticacao.addAllowedMethod("OPTIONS");
		configAutenticacao.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", configAutenticacao);

		@SuppressWarnings("unchecked")
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(-110);
		return bean;
	}
}