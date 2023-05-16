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
import com.pfi.crm.multitenant.tenant.model.Prestamo;
import com.pfi.crm.multitenant.tenant.payload.PrestamoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.PrestamoRepository;

@Service
public class PrestamoService {
	
	@Autowired
	private PrestamoRepository prestamoRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);
	
	public PrestamoPayload getPrestamoById(@PathVariable Long id) {
		return this.getPrestamoByIdModel(id).toPayload();
	}
	
	public Prestamo getPrestamoByIdModel(@PathVariable Long id) {
		return prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id));
	}
	
	public List<PrestamoPayload> getPrestamos() {
		return prestamoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public PrestamoPayload altaPrestamo(PrestamoPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null a dar de alta. Verifique el dato introducido.");
		if(payload != null && payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de prestamo: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		//return altaOModificarPrestamo(payload).toPayload();
		return prestamoRepository.save(new Prestamo(payload)).toPayload();
	}
	
	public String bajaPrestamo(Long id) {
		Prestamo m = prestamoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Prestamo", "id", id));
		String message = "Se ha dado de baja al prestamo id: " + m.getId();
		if(m.getPrestamista() != null || m.getPrestatario() != null) {
			if(m.getPrestamista()!=null) {
				message += ", y desasociado a su prestamista id: " + m.getPrestamista().getId();
				m.setPrestamista(null);
			}
			if(m.getPrestatario() != null) {
				message += ", y desasociado a su prestatario id: " + m.getPrestamista().getId();
				m.setPrestatario(null);
			}
			prestamoRepository.save(m);
		}
		prestamoRepository.delete(m);
		return message;
	}
	
	public String quitarContactoDeSusPrestamos(Long idContacto) {
		List<Prestamo> prestamosPrestamista = prestamoRepository.findByPrestamista_Id(idContacto);
		String  message = "";
		if(!prestamosPrestamista.isEmpty()) {
			message += "Se ha desasociado al contacto id '" +  idContacto + "' como prestamista";
			if(prestamosPrestamista.size()>1)
				message += " de los prestamos id's: ";
			else
				message += " del prestamo id: ";
			for(int i=0; i<prestamosPrestamista.size(); i++) {
				message += prestamosPrestamista.get(i).getId();
				if(i<prestamosPrestamista.size()-1)//no sea ultimo
					message += ", ";
				prestamosPrestamista.get(i).setPrestamista(null);
			}
			prestamoRepository.saveAll(prestamosPrestamista);
		}
		
		List<Prestamo> prestamosPrestatario = prestamoRepository.findByPrestatario_Id(idContacto);
		if(!prestamosPrestatario.isEmpty()) {
			if(!message.isEmpty())
				message += ". También se";
			else
				message += "Se";
			message += " ha desasociado al contacto id '" + idContacto + "' como prestatario";
			if(prestamosPrestatario.size()>1)
				message += " de los prestamos id's: ";
			else
				message += " del prestamo id: ";
			for(int i=0; i<prestamosPrestatario.size(); i++) {
				message += prestamosPrestatario.get(i).getId();
				if(i<prestamosPrestatario.size()-1)//no sea ultimo
					message += ", ";
				prestamosPrestatario.get(i).setPrestamista(null);
			}
			prestamoRepository.saveAll(prestamosPrestatario);
		}
		return message;
	}
	
	public PrestamoPayload modificarPrestamo(PrestamoPayload payload) {
		return this.altaOModificarPrestamo(payload).toPayload();
	}
	
	private Prestamo altaOModificarPrestamo(PrestamoPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null, no se realizará ninguna acción.");
		Prestamo prestamoModel = null;
		if(payload.getId() != null) {//Modificar
			prestamoModel = this.getPrestamoByIdModel(payload.getId());
			prestamoModel.modificar(payload);
		}
		if(prestamoModel == null){//Alta
			prestamoModel = new Prestamo(payload);
		}
		
		//Busco/alta de su prestamista
		prestamoModel.setPrestamista(contactoService.buscarOAlta(payload.getPrestamista()));
		
		//Busco/alta de su prestatario
		prestamoModel.setPrestatario(contactoService.buscarOAlta(payload.getPrestatario()));
		
		
		return prestamoRepository.save(prestamoModel);
	}
}
