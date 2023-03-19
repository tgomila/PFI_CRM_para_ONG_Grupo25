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
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.DonacionPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ContactoRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.DonacionRepository;

@Service
public class DonacionService  {
	
	@Autowired
	private DonacionRepository donacionRepository;
	
	@Autowired
	private ContactoRepository contactoRepository;
	
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
		if(payload == null)
			throw new BadRequestException("Donación ingresada no debe ser Null");
		payload.setId(null);
		
		return altaOModificarDonacion(payload).toPayload();
	}
	
	public void bajaDonacion(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Donacion m = donacionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Donacion", "id", id));
		m.setDonante(null);
		donacionRepository.save(m);
		donacionRepository.delete(m);	//Temporalmente se elimina de la BD
		
	}
	
	public DonacionPayload modificarDonacion(DonacionPayload payload) {
		if(payload == null)
			throw new BadRequestException("Donación ingresada no debe ser Null");
		if (payload.getId() == null)
			throw new BadRequestException("No se puede modificar Donación sin ID");
		return altaOModificarDonacion(payload).toPayload();
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
	
	public boolean existeDonacionesPorIdContacto(Long id) {
		return donacionRepository.existsByDonante_Id(id);
	}
	
	
	
	
	private Donacion altaOModificarDonacion(DonacionPayload payload) {
		Donacion donacionModel = null;
		if(payload.getId() != null) {//Modificar
			donacionModel = donacionRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Donacion", "id", payload.getId()));
			donacionModel.modificar(payload);
		}
		if(donacionModel == null) {//Alta
			donacionModel = new Donacion(payload);
		}
		
		//Busco su donante
		Contacto donante = null;
		if(payload.getDonante() != null) {
			if(payload.getDonante().getId() != null) {//Agrego su donante
				donante = contactoRepository.findById(payload.getDonante().getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", payload.getDonante().getId()));
			}
			else {//Doy de alta su donante (Contacto)
				ContactoPayload altaProveedor = contactoService.altaContacto(payload.getDonante());
				donante = contactoRepository.findById(altaProveedor.getId()).orElseThrow(
		                () -> new ResourceNotFoundException("Contacto", "id", altaProveedor.getId()));
			}
		}
		else {
			donante = null;
		}
		donacionModel.setDonante(donante);
		
		return donacionRepository.save(donacionModel);
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
