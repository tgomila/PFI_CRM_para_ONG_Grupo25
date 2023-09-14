package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.FacturaItem;
import com.pfi.crm.multitenant.tenant.payload.FacturaItemPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.FacturaItemRepository;

@Service
public class FacturaItemService {
	
	@Autowired
	private FacturaItemRepository facturaItemRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FacturaItemService.class);
	

	public FacturaItemPayload getFacturaItemById(@PathVariable Long id) {
		return this.getFacturaItemModelById(id).toPayload();
	}
	
	public FacturaItem getFacturaItemModelById(@PathVariable Long id) {
		return facturaItemRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("FacturaItem", "id", id));
	}
	
	public List<FacturaItemPayload> getFacturaItems() {
		return facturaItemRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public FacturaItemPayload altaFacturaItem (FacturaItemPayload payload) {
		payload.setId(null);
		return facturaItemRepository.save(new FacturaItem(payload)).toPayload();
	}
	
	public String bajaFacturaItem(Long id) {
		FacturaItem m = this.getFacturaItemModelById(id);
		facturaItemRepository.delete(m);	//Temporalmente se elimina de la BD
		return "Se ha dado de baja el item id: " + id;
	}
	
	public FacturaItemPayload modificarFacturaItem(FacturaItemPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de Contactos cliente y emisor o se crearia/modificaría un contacto
			FacturaItem model = this.getFacturaItemModelById(payload.getId());;
			model.modificar(payload);
			return facturaItemRepository.save(model).toPayload();	
		}
		throw new BadRequestException("No se puede modificar FacturaItem sin ID");
	}

}
