package com.pfi.crm.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;

@Component
public class ModuloEnumConverter implements Converter<String, ModuloEnum> {
	
	@Override
	public ModuloEnum convert(String source) {
		try {
			return ModuloEnum.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
		
	}
}