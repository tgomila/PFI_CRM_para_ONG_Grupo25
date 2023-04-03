package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Actividad;
import com.pfi.crm.multitenant.tenant.model.ProgramaDeActividades;
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;
import com.pfi.crm.multitenant.tenant.payload.ProgramaDeActividadesPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProgramaDeActividadesRepository;

@Service
public class ProgramaDeActividadesService {
	
	@Autowired
	private ProgramaDeActividadesRepository programaDeActividadesRepository;
	
	@Autowired
	private ActividadService actividadService;
	
	
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProgramaDeActividades.class);
	
	public ProgramaDeActividadesPayload getProgramaDeActividadesById(@PathVariable Long id) {
		return this.getProgramaDeActividadesModelById(id).toPayload();
	}
	
	public ProgramaDeActividades getProgramaDeActividadesModelById(@PathVariable Long id) {
		return programaDeActividadesRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("ProgramaDeActividades", "id", id));
	}
	
	public ProgramaDeActividadesPayload altaProgramaDeActividades(ProgramaDeActividadesPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como Programa de actividades a dar de alta, por favor ingrese un Programa de actividades válido.");
		if(payload != null && payload.getId() != null)
			throw new BadRequestException("Ha introducido ID de Programa de actividades: " + payload.getId() + ". ¿No querrá decir modificar en vez de alta?");
		
		return altaModificarProgramaDeActividades(payload).toPayload();
	}
	
	public void bajaProgramaDeActividades(Long id) {
		ProgramaDeActividades model = this.getProgramaDeActividadesModelById(id);
		model.setEstadoActivoPrograma(false);
		model.getActividades().forEach(act -> actividadService.bajaActividad(act.getId()));
		model.setActividades(null);
		programaDeActividadesRepository.save(model);
		programaDeActividadesRepository.delete(model);
	}
	
	public ProgramaDeActividadesPayload modificarProgramaDeActividades(ProgramaDeActividadesPayload payload) {
		if (payload != null && payload.getId() == null)
			throw new BadRequestException("No ha introducido el ID. ¿No querrá decir Alta en vez de Modificar?");
		return altaModificarProgramaDeActividades(payload).toPayload();
	}
	
	public ProgramaDeActividades altaModificarProgramaDeActividades(ProgramaDeActividadesPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como Programa de actividades, por favor ingrese un Programa de actividades válido.");
		
		//Dar de alta a actividades que no tengan id (nuevas actividades)
		List<Actividad> actividadesDelPrograma = new ArrayList<Actividad>();
		if(payload.getActividades() != null) {
			for(ActividadPayload actividadPayload: payload.getActividades()) {
				Actividad actividadModel = null;
				if(actividadPayload.getId() == null) //Si la actividad no existe en la BD.
					actividadModel = actividadService.altaActividadModel(actividadPayload);
				else
					actividadModel = actividadService.modificarActividadReturnModel(actividadPayload);
				actividadesDelPrograma.add(actividadModel);
			}
		}
		
		ProgramaDeActividades model;
		if(payload.getId() != null) { //Modificar
			model = this.getProgramaDeActividadesModelById(payload.getId());
			model.modificar(payload, actividadesDelPrograma);
		}
		else { //Alta
			model = new ProgramaDeActividades(payload, actividadesDelPrograma);
			model.setEstadoActivoPrograma(true);
		}
		
		return programaDeActividadesRepository.save(model);
	}
	
}
