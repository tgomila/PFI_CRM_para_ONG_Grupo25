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
		if(payload == null)
			throw new BadRequestException("Ha introducido un null, no se realizará ninguna acción.");
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
			else {//Tiene que ser un donante precargado, no permito ABM de contactos.
				//donante = contactoService.altaContactoModel(payload.getDonante());////Doy de alta su donante (Contacto)
				donante = null;
			}
		}
		else {
			donante = null;
		}
		donacionModel.setDonante(donante);
		
		return donacionRepository.save(donacionModel);
	}
	
	//Hacer donaciones anónimas.
	public String quitarContactoDeSusDonaciones(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		String message = "";
		List<Donacion> donacionesDelContacto = donacionRepository.findByDonante_Id(idContacto);
		if(!donacionesDelContacto.isEmpty()) {
			message += "Se ha desasociado al contacto id '" +  idContacto + "' como donante de ";
			if(donacionesDelContacto.size()>1)
				message += " sus donaciones id's: ";
			else
				message += " su donación id: ";
			for(int i=0; i<donacionesDelContacto.size();i++) {
				message += donacionesDelContacto.get(i).getId();
				if(i<donacionesDelContacto.size()-1)//no sea ultimo
					message += ", ";
				donacionesDelContacto.get(i).setDonante(null);
			}
			donacionRepository.saveAll(donacionesDelContacto);
		}
		return message;
	}
	
	public String bajaDonacion(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Donacion m = this.getDonacionModelById(id);
		String message = "Se ha dado de baja a la donación ID: '" + id + "'";
		if(m.getDonante() != null) {
			if(m.getDonante().getEmail()!=null)
				message += ", y desasociado su donante cuyo email fue: '" + m.getDonante().getEmail() + "'";
			m.setDonante(null);
			m = donacionRepository.save(m);
		}
		donacionRepository.delete(m);	//Temporalmente se elimina de la BD
		
		return message;
	}
	
	public String bajaDonacionesPorContacto(Long id) {
		List<Donacion> donacionesAModificar = donacionRepository.findByDonante_Id(id);
		String message = "";
		if(!donacionesAModificar.isEmpty()) {
			donacionesAModificar.forEach((donacion) -> donacion.setDonante(null));
			donacionesAModificar = donacionRepository.saveAll(donacionesAModificar);
			donacionRepository.deleteAll(donacionesAModificar);
			message = "Se ha dado de baja a las donaciones por contacto ID: '" + id + "'";
		}
		else {
			message = "No existen donantes con Contacto ID: '" + id + "' para dar de baja";
			throw new AppException("No existen donantes con Contacto ID: " + id + " para dar de baja.");
		}
		return message;
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
