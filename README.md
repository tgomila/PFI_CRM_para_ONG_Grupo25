<!-- Basado en template de este link https://github.com/othneildrew/Best-README-Template/blob/master/README.md-->
<a name="readme-top"></a>

# PFI_CRM_para_ONG_Grupo25
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Tabla de Contenidos</summary>
  <ol>
    <li>
      <a href="#sobre-el-proyecto">Sobre el Proyecto</a>
      <ul>
        <li><a href="#introducción">Introducción</a></li>
        <li><a href="#objetivos">Objetivos</a></li>
        <li><a href="#alcance">Alcance</a></li>
        <li><a href="#construido-en">Construido en</a></li>
      </ul>
    </li>
    <li>
      <a href="#para-empezar">Para Empezar</a>
      <ul>
        <li><a href="#instrucción-cómo-correr-el-backend">Instrucción cómo correr el backend</a></li>
        <ul>
          <li><a href="#requisitos">Requisitos</a></li>
          <li><a href="#instrucciones">Instrucciones</a></li>
        </ul>
        <li><a href="#instrucción-cómo-correr-el-frontend">Instrucción cómo correr el frontend</a></li>
      </ul>
    </li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contacto">Contacto</a></li>
  </ol>
</details>

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>



<!-- SOBRE EL PROYECTO -->
## Sobre el Proyecto

### Introducción

El proyecto Cosmos consiste en el desarrollo de un software llamado CRM

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>



### Objetivos
El objetivo general del proyecto Cosmos es el aporte una solución a las ONG con problemas de centralización de información y necesidad de mejorar su comunicación con todas las partes involucradas en la organización por medio de un software CRM.
Objetivos específicos:
* Desarrollar un sistema CRM basado en web para ONGs intuitivo y fácil de utilizar, con módulos estandarizados y personalizables.
* Solucionar problemas de comunicación y centralización de la información. 
:smile:

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>



### Alcance
El proyecto Cosmos incluye el desarrollo de un Web Based CRM con módulos estándares y módulos personalizables. Dicho sistema se desarrolló en idioma español, para uso local en Argentina, y especializado para ONGs únicamente.

Los módulos que se incluyen son:
* Beneficiarios.
* Donantes.
* Voluntarios/Colaboradores.
* Contactos.
* Conversaciones.
* Actividades/Programa de actividades.
* Factura.
* Donación.
* Inventario (producto).
* Proyectos.

Funciones del sistema:
* ABM usuarios.
* Opción para añadir módulos personalizables.
* Tutorial en video para facilitar el aprendizaje a los usuarios del sistema.

Este proyecto deja fuera de su alcance:
* Implementación/Instalación del software en una organización.
* Desarrollo o aporte de hardware.
* Actualizaciones al primer Release.
* Soporte técnico.

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>



### Construido en
* Frontend: React
* Backend: Java 11 con framework SpringBoot.
* Base de Datos: MySQL & PostgreSQL.

Para más información leer la documentación.

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>

<!-- GETTING STARTED -->
## Para Empezar

### Instrucción cómo correr el backend

#### Requisitos:
* Tener eclipse ide (se utilizó una versión del 2022).
* Base de datos MySQL Workbench (en este caso se usó la versión 8.0). Setear username "root" y password "1234" para este ejemplo, luego cambiar contraseña a una más fuerte.
* Base de datos PostgreSQL.
* Java SDK 11 en adelante (se utilizó Java 18).

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>



#### Instrucciones:
1)	Descargar el proyecto backend.
2)	Abrir eclipse, luego File --> import --> existing maven projects --> Browse --> abrir la carpeta donde descargaste el proyecto.
3)	En MySQL: 
    *	File --> New Query Tab --> y ejecutá el siguiente comando:
          ```sql
          create database master_db;
          CREATE TABLE  `master_db`.`tbl_tenant_master` (
            `tenant_client_id` int(10) unsigned NOT NULL,
            `db_name` varchar(50) NOT NULL,
            `url` varchar(100) NOT NULL,
            `user_name` varchar(50) NOT NULL,
            `tenant_name` varchar(100) NOT NULL,
            `password` varchar(100) NOT NULL,
            `driver_class` varchar(100) NOT NULL,
            `status` varchar(10) NOT NULL,
            PRIMARY KEY (`tenant_client_id`) USING BTREE
          ) ENGINE=InnoDB;
          ```
    *	A la izquierda en "Schemas" --> (click derecho) create Schema --> create un schema con los siguientes nombres: "tenant1", "tenant2", "tenant3".
4)	Luego volve al proyecto en eclipse, anda al archivo en carpeta com.pfi.crm.multitenant.tenant.config."TenantDatabaseConfig.java".
    *	En casi las últimas líneas vas a encontrar 
          ```java
          properties.put(Environment.HBM2DDL_AUTO, "none");
          ```
          cambia el ``` "none" ``` a ``` "create" ```. De esta forma se crearán las tablas en la base de datos. Debería quedar así:
          ```java
          properties.put(Environment.HBM2DDL_AUTO, "create");
          ```
5)	Luego encendé el proyecto hasta que salga el mensaje de reiniciar (apagar server y volver a encender)

    ![Paso5_Reinicio1][paso-5-reinicio-1-screenshot]
    
    * Realizalo aproximadamente 2 o 3 veces hasta que salga el siguiente mensaje de cambiar "create" a "none".
    
    ![Paso5_Reinicio2][paso-5-reinicio-final-screenshot]
    
    * Apague el servidor y pase al paso 6 (es el paso de la captura de pantalla de arriba).
    
6)	(similar al paso 4) En eclipse, anda al archivo en carpeta com.pfi.crm.multitenant.tenant.config."TenantDatabaseConfig.java".
    *	En casi las últimas líneas vas a encontrar:
          ```java
          properties.put(Environment.HBM2DDL_AUTO, "create");
          ```
    *	Cambia el ``` "create" ``` a ``` "none" ```. De esta forma ya no se borrarán los datos cada vez que se reinicie el servidor backend de eclipse.
          ```java
          properties.put(Environment.HBM2DDL_AUTO, "none");
          ```
14) Listo, ya podes ejecutar el proyecto con ejemplos precargados para probar y jugar! 😄

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>

### Instrucción cómo correr el frontend

```java
//TODO pendiente realizar a futuro.
//En pocas palabras si se requiere ya y quien tenga conocimiento, es:
// - Importar el proyecto, ejecutar comando "npm install" y corregir errores que salgan en consola googleando.
// - Luego "npm start" y arrancar server.
```

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>


<!-- ROADMAP -->
## Roadmap

- [x] Base de Datos
- [x] Backend multitenancy
- [x] Frontend multitenancy
- [x] Front de personas, beneficiarios, etc.
- [x] Actividades y programa (list actividades) de actividades.
- [x] Producto (Incluye información de inventario como cantidad, precio, proveedor).
- [x] Factura.
- [x] Préstamos.
- [x] Insumos.
- [x] Proyectos.
- [x] Gráficos front (hecho en algunos módulos).
- [x] Manejo visibilidad de módulos.
- [ ] OAuth
- [ ] Conversaciones (chat springboot o de nube).
- [ ] Tutoriales de video

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>



<!-- CONTACT -->
## Contacto

Tomás Gomila - [Facebook](https://www.facebook.com/tomas.gomila) - [Instagram](https://www.instagram.com/tomas.gomila/) - tomasgomila@gmail.com

Link del Proyecto: [https://github.com/tgomila/PFI_CRM_para_ONG_Grupo25](https://github.com/tgomila/PFI_CRM_para_ONG_Grupo25)

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>


<!-- LINKS & IMAGES -->
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/tomas-gomila/

[paso-5-reinicio-1-screenshot]: images/Paso5_Reinicio1.PNG
[paso-5-reinicio-final-screenshot]: images/Paso5_ReinicioFin.PNG
<!-- OLD IMAGES -->
[paso-tenant3-mysql-screenshot]: images/PasoTenant3MySQL.png
[paso-tenant2-mysql-screenshot]: images/PasoTenant2MySQL.png
[paso-tenant1-mysql-screenshot]: images/PasoTenant1MySQL.png
[paso-tenants-mysql-screenshot]: images/PasoTenantsMySQL.png
