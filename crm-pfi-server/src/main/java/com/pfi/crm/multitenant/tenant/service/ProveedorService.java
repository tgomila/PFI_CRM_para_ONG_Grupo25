package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProveedorRepository;

@Service
public class ProveedorService {
	
	@Autowired
	private ProveedorRepository proveedorRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);
	
	public List<ContactoPayload> getAllProveedores() {
		return proveedorRepository.findAllProveedores().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public List<Map<String, Object>>  getAllProveedoresConNumeroDeProductos() {
		return proveedorRepository.findAllProveedoresWithNumeroDeProductos();
	}
	
	public List<ProductoPayload>  getAllProveedorProductos(Long id) {
		return proveedorRepository.findProductosByProveedorId(id).stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
}