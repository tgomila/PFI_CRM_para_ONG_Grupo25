package com.pfi.crm.multitenant.tenant.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum  RoleName {
	//Prioridad (nro mas alto es igual a más prioridad)
	ROLE_DEFAULT	("Rol default indefinido", 	0), //indefinido
	ROLE_USER		("Rol usuario",		  	1),	//indefinido que hace, pero diria que nada de nada salvo leer unas cosas como noticias.
	ROLE_PROFESIONAL("Rol Profesional",	   10), //Da actividades a beneficiarios.
	ROLE_EMPLOYEE	("Rol Empleado",	  100),	//abm de clases models.
	ROLE_ADMIN		("Rol Administrador",1000);	//Dueño/Presidente ONG, abm de los de arriba.
	
	//Opcional más adelante
	//ROLE_COLABORADOR
	//ROLE_DONANTE
	//ROLE_PERSONA
	//ROLE_EMPRESA
	//ROLE_BENEFICIARIO, //Leer noticias y a que actividades esta involucrado.
	//ROLE_MODERATOR,	//Jefes, abm de los de arriba.//ROLE_COLABORADOR, 
	//ROLE_VOLUNTARIO,
	
	private final String name;
	private final int priority;
	
	private RoleName(String name, int priority) {
		this.name = name;
		this.priority = priority;
	}
	
	public static Comparator<RoleName> priorityComparator = new Comparator<RoleName>() {
		public int compare(RoleName r1, RoleName r2) {
		  return r1.getPriority() - r2.getPriority();
		}
	  };

	public int compare(RoleName that) {
		return Integer.compare(this.priority, that.priority);
	}
	
	public RoleName max(List<RoleName> roles) {
		RoleName max = Collections.max(roles, Comparator.comparingInt(RoleName::getPriority));
		return max;
	}

	public String getName() {
		return name;
	}

	public int getPriority() {
		return priority;
	}
}
