package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Donacion;
import com.pfi.crm.multitenant.tenant.payload.DonacionPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.DonacionRepository;

@Service
public class DonacionService  {
	
	@Autowired
	private DonacionRepository donacionRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(DonacionService.class);
	
	public DonacionPayload getDonacionById(@PathVariable Long id) {
        return donacionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Donacion", "id", id)).toPayload();
    }
	
	public List<DonacionPayload> getDonaciones() {
		//return donacionRepository.findAll();
		return donacionRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public DonacionPayload altaDonacion (DonacionPayload payload) {
		payload.setId(null);
		Donacion model = new Donacion();
		// Busco el contacto en la BD y tomo los datos de ahi, porque sino me pueden modificar el
		// "contacto" sin permiso
		Contacto c;
		if(payload.getDonante() != null && payload.getDonante().getId() != null) {
			c = contactoService.getContactoById(payload.getId());
			if(c != null)
				model.setDonante(c);
			else
				model.setDonante(null);
		}
		else {
			c = null;
		}
		return donacionRepository.save(model).toPayload();
	}
	
	public void bajaDonacion(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Donacion> optionalModel = donacionRepository.findById(id);
		if(optionalModel.isPresent()) {
			Donacion m = optionalModel.get();
			donacionRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona Fisica
		}
		
	}
	
	public DonacionPayload modificarDonacion(DonacionPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<Donacion> optional = donacionRepository.findById(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Donacion model = optional.get();
				model.modificar(payload);
				return donacionRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	// Conversiones Payload Model
	/*public Donacion toModel(DonacionPayload p) {

		Donacion m = new Donacion();
		
		m.setId(p.getId());
		m.setFecha(p.getFecha());
		m.setDonante(new Contacto(p.getDonante()));
		m.setTipoDonacion(p.getTipoDonacion());
		m.setDescripcion(p.getDescripcion());

		return m;
	}

	public DonacionPayload toPayload(Donacion m) {

		DonacionPayload p = new DonacionPayload();

		p.setId(m.getId());
		p.setFecha(m.getFecha());
		p.setDonante(m.getDonante().toPayload());
		p.setTipoDonacion(m.getTipoDonacion());
		p.setDescripcion(m.getDescripcion());

		return p;
	}*/
}
