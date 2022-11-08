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
* Talleres/Actividades.
* Inventario.
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
* Backend: Java 18 con framework SpringBoot.
* Base de Datos: MySQL.

Para más información leer la documentación.

<p align="right">(<a href="#readme-top">volver a arriba</a>)</p>

<!-- GETTING STARTED -->
## Para Empezar

### Instrucción cómo correr el backend

#### Requisitos:
* Tener eclipse ide (se utilizó una versión del 2022).
* Base de datos MySQL Workbench (en este caso se usó la versión 8.0). Setear username "root" y password "1234" para este ejemplo, luego cambiar contraseña a una más fuerte.
* Java SDK 18.

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
5)	Luego anda a ``` com.pri.crm.cargarDatosEjemplo.java ```:
    *	Allí dentro primero ejecuta el código así, agregando “//” a tenants 1 y 2
        ```java
        //cargarTenant1();
        //cargarTenant2();
        cargarTenant3();
        ```
    *	En la base de datos “master_db” anda a la tabla y agrega este dato:
    
        [![PasoTenant3MySQL][paso-tenant3-mysql-screenshot]]
        ```
        '300', 'tenant3', 'ONG Sapito', 'jdbc:mysql://localhost:3306/tenant3?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
        ```
6)	Ahora volve a eclipse, com.pri.crm.PfiApplication.java, hace click derecho --> Run As --> Spring Boot App.
7)	Una vez que se haya ejecutado, dale a “Stop”. Ya se crearon las tablas para “tenant3”, ahora hay que crear las tablas para tenant2 y tenant1.
8)	En MySQL en la tabla de master_db, modifica los datos del tenant1 y que sean para tenant2:
    ```
    '200', 'tenant2', 'ONG Comida para los chicos', 'jdbc:mysql://localhost:3306/tenant2?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
    ```
    * En MySQL debería verse así:
    
        [![PasoTenant2MySQL][paso-tenant2-mysql-screenshot]]
    * En eclipse así:
        ```java
        //cargarTenant1();
        cargarTenant2();
        //cargarTenant3();
        ```
    * Ahora ejecuta eclipse (igual a paso 6).
9)	Una vez que se haya ejecutado, dale a “Stop”. Ya se crearon las tablas para “tenant2”, ahora hay que crear las tablas para tenant1.
10)	En MySQL en la tabla de master_db, modifica los datos del tenant2 y que sean para tenant1:
    ```
    '100', 'tenant1', 'ONG Mi Arbolito', 'jdbc:mysql://localhost:3306/tenant1?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
    ```
    * En MySQL debería verse así:
    
        [![PasoTenant1MySQL][paso-tenant1-mysql-screenshot]]
    * En eclipse así:
        ```java
        cargarTenant1();
        //cargarTenant2();
        //cargarTenant3();
        ```
    * Ahora ejecuta eclipse (igual a paso 6).
11) Una vez que se haya ejecutado, dale a “Stop”. Ya se crearon las tablas para “tenant1”.
12) En MySQL en la tabla de master_db, agrega todos los tenants. Se deja un ejemplo que se puede copiar:
    ```
    '100', 'tenant1', 'ONG Mi Arbolito', 'jdbc:mysql://localhost:3306/tenant1?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
    '200', 'tenant2', 'ONG Comida para los chicos', 'jdbc:mysql://localhost:3306/tenant2?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
    '300', 'tenant3', 'ONG Sapito', 'jdbc:mysql://localhost:3306/tenant3?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false', 'root', '1234', 'com.mysql.cj.jdbc.Driver', 'Active'
    ```
    * En MySQL debería verse así:
    
        [![PasoTenantsMySQL][paso-tenants-mysql-screenshot]]
    * En eclipse así:
        ```java
        cargarTenant1();
        cargarTenant2();
        cargarTenant3();
        ```
13) (similar al paso 4) En eclipse, anda al archivo en carpeta com.pfi.crm.multitenant.tenant.config."TenantDatabaseConfig.java".
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
//TODO
```

<p align="right">(<a href="#readme-top">volver arriba</a>)</p>


<!-- ROADMAP -->
## Roadmap

- [x] Base de Datos
- [x] Backend multitenancy
- [x] Frontend multitenancy
- [ ] Conversaciones (chat springboot o de nube)
- [ ] Actividades y programa actividades (list actividades).
- [ ] Taller/Actividad
- [ ] Inventario (Producto, cantidad, precio, notas, subtotal) --> Clase producto
- [ ] Préstamos
- [ ] Insumos
- [ ] Proyectos
- [ ] OAuth
- [ ] Módulos
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
[paso-tenant3-mysql-screenshot]: images/PasoTenant3MySQL.png
[paso-tenant2-mysql-screenshot]: images/PasoTenant2MySQL.png
[paso-tenant1-mysql-screenshot]: images/PasoTenant1MySQL.png
[paso-tenants-mysql-screenshot]: images/PasoTenantsMySQL.png
