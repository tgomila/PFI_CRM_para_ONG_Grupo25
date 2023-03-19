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
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Factura;
import com.pfi.crm.multitenant.tenant.payload.FacturaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.FacturaRepository;

@Service
public class FacturaService {
	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private ContactoRepository contactoRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);
	

	public FacturaPayload getFacturaById(@PathVariable Long id) {
        return facturaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Factura", "id", id)).toPayload();
    }
	
	public List<FacturaPayload> getFacturas() {
		return facturaRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public FacturaPayload altaFactura (FacturaPayload payload) {
		if(payload != null && payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de factura: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		return altaModificarFactura(payload);
	}
	
	public void bajaFactura(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Factura m = facturaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Factura", "id", id));
		m.setCliente(null);
		m.setEmisorFactura(null);
		facturaRepository.save(m);
		facturaRepository.delete(m);	//Temporalmente se elimina de la BD
		
	}
	
	public FacturaPayload modificarFactura(FacturaPayload payload) {
		if (payload != null && payload.getId() == null)
			throw new BadRequestException("No ha introducido el ID. ¿No querrá decir Alta en vez de Modificar?");
		return altaModificarFactura(payload);
		/*if (payload != null && payload.getId() != null) {
			//Necesito el id de Contactos cliente y emisor o se crearia/modificaría un contacto
			Factura model = facturaRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Factura", "id", payload.getId()));
			
			Contacto cliente = null;
			if(payload.getCliente() != null && payload.getCliente().getId() != null) {
				cliente = contactoRepository.findById(payload.getCliente().getId()).orElseThrow(
	                () -> new ResourceNotFoundException("Contacto", "id", payload.getCliente().getId()));
			}
			Contacto emisorFactura = null;
			if(payload.getEmisorFactura() != null && payload.getEmisorFactura().getId() != null) {
				cliente = contactoRepository.findById(payload.getEmisorFactura().getId()).orElseThrow(
	                () -> new ResourceNotFoundException("Contacto", "id", payload.getCliente().getId()));
			}
			
			//chequear items de factura
			List<FacturaItem> itemsFacturaModificado = new ArrayList<FacturaItem>();
			
			for(FacturaItemPayload itemPayload: payload.getItemsFactura()) {
				FacturaItem item = null;
				//Si no existe, lo creo
				if(itemPayload.getId() == null) {
					item = facturaItemRepository.save(new FacturaItem(itemPayload));
				}
				else {
					//existe item, lo busco si existe dado de alta
					for(FacturaItem itemModel: model.getItemsFactura()) {
						if(itemModel.getId().equals(itemPayload.getId()) ){
							itemModel.modificar(itemPayload);
							item = facturaItemRepository.save(itemModel);
						}
					}
					//Si no se encuentra en itemModel y hay asifnado un id, le saco el id
					if(item == null) {
						item = facturaItemRepository.save(new FacturaItem(itemPayload));
					}
				}
				//se asume que item ya no es null
				itemsFacturaModificado.add(item);
				
			}
			model.modificar(payload, cliente, emisorFactura, itemsFacturaModificado);
			return facturaRepository.save(model).toPayload();	
		}
		throw new BadRequestException("No se puede modificar Factura sin ID");*/
	}
	
	private FacturaPayload altaModificarFactura(FacturaPayload payload) {
		//Primero evito modificar el contacto, sino lo creo
		Contacto cliente = null;
		if(payload.getCliente() != null) {
			if(payload.getCliente().getId() != null) {
				cliente = contactoRepository.findById(payload.getCliente().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Contacto", "id", payload.getCliente().getId()));
			}
			else {
				cliente = new Contacto(payload.getCliente());
			}
		}
		Contacto emisorFactura = null;
		if(payload.getEmisorFactura() != null) {
			if(payload.getCliente().getId() != null) {
				emisorFactura = contactoRepository.findById(payload.getEmisorFactura().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Contacto", "id", payload.getCliente().getId()));
			}
			else {
				emisorFactura = new Contacto(payload.getEmisorFactura());
			}
		}
		Factura factura = new Factura(payload, cliente, emisorFactura);
		factura.setId(payload.getId());//Por si es "Modificar", si es alta solo seteo un null.
		return facturaRepository.save(factura).toPayload();
		
	}
	
	public void quitarContactoDeSusFacturas(Long idContacto) {
		List<Factura> facturasCliente = facturaRepository.findByCliente_Id(idContacto);
		if(!facturasCliente.isEmpty()) {
			facturasCliente.forEach((factura) -> factura.setCliente(null));
			facturaRepository.saveAll(facturasCliente);
		}
		List<Factura> facturasEmisor = facturaRepository.findByEmisorFactura_Id(idContacto);
		if(!facturasEmisor.isEmpty()) {
			facturasEmisor.forEach((factura) -> factura.setEmisorFactura(null));
			facturaRepository.saveAll(facturasEmisor);
		}
	}
	
	public boolean existeFacturaPorIdContacto(Long id) {
		return facturaRepository.existsByCliente_Id(id);
	}
}