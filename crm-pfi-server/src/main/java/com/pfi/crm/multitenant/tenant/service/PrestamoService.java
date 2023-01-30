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
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.PrestamoRepository;

@Service
public class PrestamoService {
	
	@Autowired
	private PrestamoRepository prestamoRepository;
	
	@Autowired
	private ContactoRepository contactoRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);
	
	public PrestamoPayload getPrestamoById(@PathVariable Long id) {
		return prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id)).toPayload();
	}
	
	public List<PrestamoPayload> getPrestamos() {
		return prestamoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public PrestamoPayload altaPrestamo(PrestamoPayload p) {
		p.setId(null);
		Prestamo m = new Prestamo();
		return prestamoRepository.save(m).toPayload();
	}
	
	public void bajaPrestamo(Long id) {
		Prestamo m = prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id));
		prestamoRepository.delete(m);
	}
	
	public PrestamoPayload modificarPrestamo(PrestamoPayload payload) {
		return this.modificarPrestamoReturnModel(payload).toPayload();
	}
	
	public Prestamo modificarPrestamoReturnModel(PrestamoPayload payload) {
		Prestamo model = prestamoRepository.findById(payload.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", payload.getId()));
		
		//Esto evita que puedan modificar models beneficiarios y profesional
		Contacto prestamista = null, prestatario = null;
		if(payload.getPrestamista() != null && payload.getPrestamista().getId() != null) {
			prestamista = contactoRepository.findById(payload.getPrestamista().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Contacto Prestamista", "id", payload.getPrestamista().getId()));
		}
		
		if(payload.getPrestatario() != null && payload.getPrestatario().getId() != null) {
			prestatario = contactoRepository.findById(payload.getPrestatario().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Contacto Prestatario", "id", payload.getPrestatario().getId()));
		}
		
		model.modificar(payload, prestamista, prestatario);
		return prestamoRepository.save(model);
	}
}
