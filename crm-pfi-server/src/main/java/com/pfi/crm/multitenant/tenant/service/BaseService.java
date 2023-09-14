package com.pfi.crm.multitenant.tenant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;

//No se va a usar esta clase, se implementará a futuro. De momento es cada service personalizado

//M model, P payload, R repository, IDTYPE (Long, Integer o int)
public abstract class BaseService<M, P, R, IDTYPE> {
	
	@Autowired
	private JpaRepository<M, IDTYPE> genericRepository;
	
	//TODO cambiar Model a Contacto por ejemplo
	public M getModelById(@PathVariable IDTYPE id) {
		return genericRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Model", "id", id));
	}
	
	abstract List<M> getAll();
	/*public List<M> getAll() {
		return genericRepository.findAll();
		//return contactoRepository.findAll().filter(a -> a.getEstadoActivoContacto()).collect(Collectors.toList());
	}*/
	
	abstract M alta(M model);
	/*public M alta (M model) {
		model.setId(null);
		model.setFechaAltaContacto(LocalDate.now());
		return contactoRepository.save(contacto);
	}*/
	
	abstract void baja(IDTYPE id);
	/*public void baja(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Contacto> optionalModel = contactoRepository.findById(id);
		if(optionalModel.isPresent()) {
			Contacto m = optionalModel.get();
			m.setEstadoActivoContacto(false);
			m.setFechaBajaContacto(LocalDate.now());
			contactoRepository.save(m);
			//contactoRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe contacto
		}
		
	}*/
	
	abstract M modificar(M model);
	/*public M modificar(M model) {
		if (model != null && model.getId() != null)
			return genericRepository.save(model);
		else
			return new M();
	}*/
	
	abstract M toModel(P payload);
	
	abstract P toPayload(M model);
}
