package com.pfi.crm.multitenant.tenant.model;

public enum  RoleName {
	ROLE_USER,	  //indefinido que hace, pero diria que nada de nada salvo leer unas cosas como noticias.
	ROLE_PROFESIONAL, //Da actividades a beneficiarios.
	ROLE_EMPLOYEE,	//abm de clases models.
	ROLE_ADMIN		//Dueño/Presidente ONG, abm de los de arriba.
	
	//Opcional más adelante
	//ROLE_COLABORADOR
	//ROLE_DONANTE
	//ROLE_PERSONA
	//ROLE_EMPRESA
	//ROLE_BENEFICIARIO, //Leer noticias y a que actividades esta involucrado.
	//ROLE_MODERATOR,	//Jefes, abm de los de arriba.//ROLE_COLABORADOR, 
	//ROLE_VOLUNTARIO,
}
