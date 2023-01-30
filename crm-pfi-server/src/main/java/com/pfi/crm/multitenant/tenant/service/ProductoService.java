package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Producto;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
	
	public ProductoPayload getProductoById(@PathVariable Long id) {
		return productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id)).toPayload();
	}
	
	public List<ProductoPayload> getProductos() {
		return productoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public ProductoPayload altaProducto(ProductoPayload p) {
		p.setId(null);
		Producto m = new Producto();
		m.setEstadoActivo(true);
		return productoRepository.save(m).toPayload();
	}
	
	public void bajaProducto(Long id) {
		Producto m = productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id));
		m.setEstadoActivo(false);
		productoRepository.save(m);
	}
	
	public ProductoPayload modificarProducto(ProductoPayload payload) {
		return this.modificarProductoReturnModel(payload).toPayload();
	}
	
	public Producto modificarProductoReturnModel(ProductoPayload payload) {
		Producto model = productoRepository.findById(payload.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", payload.getId()));
		
		model.modificar(payload);
		return productoRepository.save(model);
	}
}