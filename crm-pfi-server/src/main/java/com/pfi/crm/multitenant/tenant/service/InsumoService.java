package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Insumo;
import com.pfi.crm.multitenant.tenant.payload.InsumoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.InsumoRepository;

@Service
public class InsumoService {
	
	@Autowired
	private InsumoRepository insumoRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(InsumoService.class);
	
	public InsumoPayload getInsumoById(@PathVariable Long id) {
		return insumoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Insumo", "id", id)).toPayload();
	}
	
	public List<InsumoPayload> getInsumos() {
		return insumoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public InsumoPayload altaInsumo(InsumoPayload p) {
		p.setId(null);
		Insumo m = new Insumo();
		m.setEstadoActivo(true);
		return insumoRepository.save(m).toPayload();
	}
	
	public String bajaInsumo(Long id) {
		Insumo m = insumoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Insumo", "id", id));
		m.setEstadoActivo(false);
		insumoRepository.save(m);
		return "Se ha dado de baja al insumo id: " + id;
	}
	
	public InsumoPayload modificarInsumo(InsumoPayload payload) {
		return this.modificarInsumoReturnModel(payload).toPayload();
	}
	
	public Insumo modificarInsumoReturnModel(InsumoPayload payload) {
		Insumo model = insumoRepository.findById(payload.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Insumo", "id", payload.getId()));
		
		model.modificar(payload);
		return insumoRepository.save(model);
	}
}