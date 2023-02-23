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
import com.pfi.crm.multitenant.tenant.model.Prestamo;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.PrestamoRepository;

@Service
public class PrestamoService {
	
	@Autowired
	private PrestamoRepository prestamoRepository;
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);
	
	public PrestamoPayload getPrestamoById(@PathVariable Long id) {
		return prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id)).toPayload();
	}
	
	public List<PrestamoPayload> getPrestamos() {
		return prestamoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public PrestamoPayload altaPrestamo(PrestamoPayload payload) {
		payload.setId(null);
		
		return altaOModificarPrestamo(payload).toPayload();
	}
	
	public void bajaPrestamo(Long id) {
		Prestamo m = prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id));
		prestamoRepository.delete(m);
	}
	
	public PrestamoPayload modificarPrestamo(PrestamoPayload payload) {
		return this.altaOModificarPrestamo(payload).toPayload();
	}
	
	private Prestamo altaOModificarPrestamo(PrestamoPayload payload) {
		Prestamo prestamoModel = null;
		if(payload.getId() != null) {//Modificar
			prestamoModel = prestamoRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Prestamo", "id", payload.getId()));
			prestamoModel.modificar(payload);
		}
		if(prestamoModel == null){//Alta
			prestamoModel = new Prestamo(payload);
		}
		
		//Busco su prestamista
		Contacto prestamista = null;
		if(payload.getPrestamista() != null) {
			if(payload.getPrestamista().getId() != null) {//Agrego su prestamista
				prestamista = contactoRepository.findById(payload.getPrestamista().getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", payload.getPrestamista().getId()));
			}
			else {//Doy de alta su prestamista (Contacto)
				ContactoPayload altaPrestamista = contactoService.altaContacto(payload.getPrestamista());
				prestamista = contactoRepository.findById(altaPrestamista.getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", altaPrestamista.getId()));
			}
		}
		else {
			prestamista = null;
		}
		prestamoModel.setPrestamista(prestamista);
		
		//Busco su prestatario
		Contacto prestatario = null;
		if(payload.getPrestatario() != null) {
			if(payload.getPrestatario().getId() != null) {//Agrego su prestatario
				prestatario = contactoRepository.findById(payload.getPrestatario().getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", payload.getPrestatario().getId()));
			}
			else {//Doy de alta su prestatario (Contacto)
				ContactoPayload altaPrestatario = contactoService.altaContacto(payload.getPrestatario());
				prestatario = contactoRepository.findById(altaPrestatario.getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", altaPrestatario.getId()));
			}
		}
		else {
			prestatario = null;
		}
		prestamoModel.setPrestatario(prestatario);
		
		
		return prestamoRepository.save(prestamoModel);
	}
}
