package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.AppException;
import com.pfi.crm.exception.BadRequestException;
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
        return getDonacionModelById(id).toPayload();
    }
	
	public Donacion getDonacionModelById(Long id) {
        return donacionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Donacion", "id", id));
    }
	
	public List<DonacionPayload> getDonaciones() {
		//return donacionRepository.findAll();
		return donacionRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public DonacionPayload altaDonacion (DonacionPayload payload) {
		if(payload == null)
			throw new BadRequestException("Donación ingresada no debe ser Null");
		if(payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de donación: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		
		return altaModificarDonacion(payload).toPayload();
	}
	
	private Donacion altaModificarDonacion(DonacionPayload payload) {
		Donacion donacionModel = null;
		if(payload.getId() != null) { //Modificar
			donacionModel = this.getDonacionModelById(payload.getId());
			donacionModel.modificar(payload);
		}
		else { //Alta
			donacionModel = new Donacion(payload);
		}
		
		//Busco su donante
		Contacto donante = null;
		if(payload.getDonante() != null) {
			if(payload.getDonante().getId() != null) {//Agrego su donante
				donante = contactoService.getContactoModelById(payload.getDonante().getId());
			}
			else {//Doy de alta su donante (Contacto)
				donante = contactoService.altaContactoModel(payload.getDonante());
			}
		}
		else {
			donante = null;
		}
		donacionModel.setDonante(donante);
		
		return donacionRepository.save(donacionModel);
	}
	
	public String bajaDonacion(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Donacion m = this.getDonacionModelById(id);
		String message = "Se ha dado de baja a la donación id: " + id;
		if(m.getDonante() != null) {
			if(m.getDonante().getEmail()!=null)
				message += ", y desasociado su donante cuyo mail fue " + m.getDonante().getEmail();
			m.setDonante(null);
			m = donacionRepository.save(m);
		}
		donacionRepository.delete(m);	//Temporalmente se elimina de la BD
		
		return message;
	}
	
	public void bajaDonacionesPorContacto(Long id) {
		List<Donacion> donacionesAModificar = donacionRepository.findByDonante_Id(id);
		if(!donacionesAModificar.isEmpty()) {
			donacionesAModificar.forEach((donacion) -> donacion.setDonante(null));
			donacionesAModificar = donacionRepository.saveAll(donacionesAModificar);
			donacionRepository.deleteAll(donacionesAModificar);
		}
		else {
			throw new AppException("No existen donantes con Contacto ID: " + id + " para dar de baja.");
		}
		
	}
	
	public DonacionPayload modificarDonacion(DonacionPayload payload) {
		return this.modificarDonacionModel(payload).toPayload();
	}
	
	public Donacion modificarDonacionModel(DonacionPayload payload) {
		if(payload == null)
			throw new BadRequestException("Donación ingresada no debe ser Null");
		if (payload.getId() == null)
			throw new BadRequestException("No se puede modificar Donación sin ID");
		return altaModificarDonacion(payload);
	}
	
	public boolean existeDonacionesPorIdContacto(Long id) {
		return donacionRepository.existsByDonante_Id(id);
	}
}
