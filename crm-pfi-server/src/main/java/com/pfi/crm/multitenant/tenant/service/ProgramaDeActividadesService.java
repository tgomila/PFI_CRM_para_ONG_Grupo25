package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Actividad;
import com.pfi.crm.multitenant.tenant.model.ProgramaDeActividades;
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;
import com.pfi.crm.multitenant.tenant.payload.ProgramaDeActividadesPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProgramaDeActividadesRepository;

public class ProgramaDeActividadesService {
	
	@Autowired
	private ProgramaDeActividadesRepository programaDeActividadesRepository;
	
	@Autowired
	private ActividadService actividadService;
	
	
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProgramaDeActividades.class);
	
	public ProgramaDeActividadesPayload getProgramaDeActividadesById(@PathVariable Long id) {
		return programaDeActividadesRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("ProgramaDeActividades", "id", id)).toPayload();
	}
	
	public ProgramaDeActividadesPayload altaProgramaDeActividades(ProgramaDeActividadesPayload payload) {
		
		//Dar de alta a actividades que no tengan id (nuevas actividades)
		List<Actividad> actividadesDelPrograma = new ArrayList<Actividad>();
		for(ActividadPayload actividadPayload: payload.getActividades()) {
			Actividad actividadModel;
			if(actividadPayload.getId() == null) //Si la actividad no existe en la BD.
				actividadModel = actividadService.altaActividadModel(actividadPayload);
			else
				actividadModel = actividadService.getActividadModelById(actividadPayload.getId());
			actividadesDelPrograma.add(actividadModel);
		}
		
		payload.setId(null);
		ProgramaDeActividades model = new ProgramaDeActividades(payload, actividadesDelPrograma);
		model.setEstadoActivoPrograma(true);
		return programaDeActividadesRepository.save(model).toPayload();
	}
	
	public void bajaProgramaDeActividades(Long id) {
		ProgramaDeActividades model = programaDeActividadesRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("ProgramaDeActividades", "id", id));
		model.setEstadoActivoPrograma(false);
		model.getActividades().forEach(act -> actividadService.bajaActividad(act.getId()));
		model.setActividades(null);
		programaDeActividadesRepository.save(model);
		programaDeActividadesRepository.delete(model);
	}
	
	public ProgramaDeActividadesPayload modificarProgramaDeActividades(ProgramaDeActividadesPayload payload) {
		ProgramaDeActividades model = programaDeActividadesRepository.findById(payload.getId()).orElseThrow(
				() -> new ResourceNotFoundException("ProgramaDeActividades", "id", payload.getId()));
		
		List<Actividad> actividades = new ArrayList<Actividad>();
		if(payload.getActividades() != null) {
			payload.getActividades().forEach((p) -> actividades.add(actividadService.modificarActividadReturnModel(p)));
		}
		
		model.modificar(payload, actividades);
		return programaDeActividadesRepository.save(model).toPayload();
	}
	
}
