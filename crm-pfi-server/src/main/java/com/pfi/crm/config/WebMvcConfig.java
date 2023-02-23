package com.pfi.crm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pfi.crm.config.converter.ModuloEnumConverter;

/*
 * El proyecto React se conecta con este.
 * Muchos proyectos usan cross origin requests en los controladores.
 * Esto hace la magia.
 */
@Configuration
@EnableScheduling	//Se utiliza en Event.java para ejecutar eventos cada N minutos/horas/días/etc
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
    
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ModuloEnumConverter());
	}
}
