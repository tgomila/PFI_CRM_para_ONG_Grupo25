package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Producto;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
	
	public ProductoPayload getProductoById(@PathVariable Long id) {
		System.out.println("ID producto: " + id);
		return productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id)).toPayload();
	}
	
	public List<ProductoPayload> getProductos() {
		return productoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public ProductoPayload altaProducto(ProductoPayload payload) {
		payload.setId(null);
		return altaOModificarProducto(payload).toPayload();
		//return productoRepository.save(new Producto(payload)).toPayload();
	}
	
	public void bajaProducto(Long id) {
		Producto m = productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id));
		m.setEstadoActivo(false);
		m.setProveedor(null);
		m = productoRepository.save(m);
		productoRepository.delete(m);
	}
	
	public ProductoPayload modificarProducto(ProductoPayload payload) {
		return this.altaOModificarProducto(payload).toPayload();
		//return productoRepository.save(new Producto(payload)).toPayload();
	}
	
	
	
	
	private Producto altaOModificarProducto(ProductoPayload payload) {
		Producto productoModel = null;
		if(payload.getId() != null) {//Modificar
			productoModel = productoRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Producto", "id", payload.getId()));
			productoModel.modificar(payload);
		}
		if(productoModel == null){//Alta
			productoModel = new Producto(payload);
			productoModel.setEstadoActivo(true);
		}
		
		//Busco su proveedor
		Contacto proveedor = null;
		if(payload.getProveedor() != null) {
			if(payload.getProveedor().getId() != null) {//Agrego su proveedor
				proveedor = contactoService.getContactoModelById(payload.getProveedor().getId());
			}
			else {//Doy de alta su proveedor (Contacto)
				proveedor = contactoService.altaContactoModel(payload.getProveedor());
			}
		}
		else {
			proveedor = null;
		}
		productoModel.setProveedor(proveedor);
		
		
		return productoRepository.save(productoModel);
	}
}