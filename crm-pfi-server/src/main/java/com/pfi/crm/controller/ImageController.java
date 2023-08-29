package com.pfi.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ImagenPayload;
import com.pfi.crm.multitenant.tenant.service.FileStorageService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/images")
public class ImageController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	
	
	
	
	//Inicio Perfil
	@GetMapping("/perfil")
	public ResponseEntity<Resource> getFotoPerfil(@CurrentUser UserPrincipal currentUser) {
		//No requiere seguridad para ver su foto de perfil
		return fileStorageService.getFotoPerfilUrl(currentUser);
	}
	
	@GetMapping("/perfil/info")
	public ResponseEntity<ImagenPayload> getInfoFotoPerfil(@CurrentUser UserPrincipal currentUser) {
		//No requiere seguridad para ver su foto de perfil
		return fileStorageService.getInfoFotoPerfil(currentUser);
	}
	
	@PostMapping("/perfil")
	public String postFotoPerfil(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		//No requiere seguridad para cargar/cambiar su foto de perfil
		String message = "";
		try {
			fileStorageService.saveFotoPerfil(file, currentUser);
			 message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
		}
		return message;
	}
	
	@DeleteMapping("/perfil")
	public ResponseEntity<?> deleteFotoPerfil(@CurrentUser UserPrincipal currentUser) {
		//No requiere seguridad para borrar su propia foto de perfil
		fileStorageService.deleteFotoPerfil(currentUser);
		return ResponseEntity.ok().body(new ApiResponse(true, "La foto de perfil del contacto id: " + currentUser.getIdContacto() + " fue borrado exitosamente"));
	}
	//Fin perfil
	
	
	
	
	
	
	//Inicio Contacto
	@GetMapping("/contacto/search/{idContacto}")
	public ResponseEntity<byte[]> getFotoContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver foto de contacto en tamaño completo");
		return fileStorageService.getFotoContacto(idContacto);
	}
	
	@GetMapping("/contacto/search_tabla/{idContacto}")
	public ResponseEntity<byte[]> getFotoTablaContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver foto de contacto en tamaño tabla");
		return fileStorageService.getFotoTablaContacto(idContacto);
	}
	
	@GetMapping("/contacto/info/{idContacto}")
	public ResponseEntity<ImagenPayload> getInfoFotoContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver información de foto de contacto cargado");
		return fileStorageService.getInfoFotoContacto(idContacto);
	}
	
	@GetMapping("/contacto/info")
	public List<ImagenPayload> getInfoFotoContactos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver información de fotos de contactos cargados");
		//return fileStorageService.loadAllContactos();
		return fileStorageService.getInfoFotosGeneric("contacto");
		
		//List<FileInfoPayload> imageInfos = fileStorageService.loadAll("contacto").map(path -> {
		//	String filename = path.getFileName().toString();
		//	String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
		//			path.getFileName().toString()).build().toString();
		//	return new FileInfoPayload(filename, url, null);
		//}).collect((Collectors.toList()));
		
		//return imageInfos;
	}
	
	@PostMapping("/contacto/{id}")
	public ResponseEntity<?> uploadImageContacto(@PathVariable Long id, @RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Dar de alta/modificar una foto a contacto");
		String message = "";
		try {
			fileStorageService.saveFotoContacto(file, id);
			message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
	@DeleteMapping("/contacto/{idContacto}")
	public ResponseEntity<?> deleteFotoContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederImageContacto(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Eliminar una foto a contacto");
		String message = "";
		try {
			fileStorageService.deleteFotoContacto(idContacto);
			message = "Se ha eliminado exitosamente la foto ID: '" + idContacto +"'.";
		} catch (Exception e) {
			message = "No se pudo eliminar la foto ID: '" + idContacto + "'. Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	//Fin contacto
	
	
	
	
	
	//Inicio Producto
	@GetMapping("/producto/search/{idProducto}")
	public ResponseEntity<byte[]> getFotoProducto(@PathVariable Long idProducto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver foto de producto en tamaño completo");
		return fileStorageService.getFotoProducto(idProducto);
	}
	
	@GetMapping("/producto/search_tabla/{idProducto}")
	public ResponseEntity<byte[]> getFotoTablaProducto(@PathVariable Long idProducto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver foto de producto en tamaño tabla");
		return fileStorageService.getFotoTablaProducto(idProducto);
	}
	
	@GetMapping("/producto/info/{idProducto}")
	public ResponseEntity<ImagenPayload> getInfoFotoProducto(@PathVariable Long idProducto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver información de foto de producto cargado");
		return fileStorageService.getInfoFotoProducto(idProducto);
	}
	
	@GetMapping("/producto/info")
	public List<ImagenPayload> getInfoFotoProductos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver información de fotos de productos cargados");
		return fileStorageService.getInfoFotosGeneric("producto");
	}
	
	@PostMapping("/producto/{id}")
	public ResponseEntity<?> uploadImageProducto(@PathVariable Long id, @RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRODUCTO, "Dar de alta/modificar una foto a producto");
		String message = "";
		try {
			fileStorageService.saveFotoProducto(file, id);
			message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
	@DeleteMapping("/producto/{idProducto}")
	public ResponseEntity<?> deleteFotoProducto(@PathVariable Long idProducto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRODUCTO, "Eliminar una foto a producto");
		String message = "";
		try {
			fileStorageService.deleteFotoProducto(idProducto);
			message = "Se ha eliminado exitosamente la foto ID: '" + idProducto +"'.";
		} catch (Exception e) {
			message = "No se pudo eliminar la foto ID: '" + idProducto + "'. Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	//Fin producto
	
	
	
	
	
	
	
	
	//Inicio Actividad
	@GetMapping("/actividad/search/{idActividad}")
	public ResponseEntity<byte[]> getFotoActividad(@PathVariable Long idActividad, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver foto de actividad en tamaño completo");
		return fileStorageService.getFotoActividad(idActividad);
	}
	
	@GetMapping("/actividad/search_tabla/{idActividad}")
	public ResponseEntity<byte[]> getFotoTablaActividad(@PathVariable Long idActividad, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver foto de actividad en tamaño tabla");
		return fileStorageService.getFotoTablaActividad(idActividad);
	}
	
	@GetMapping("/actividad/info/{idActividad}")
	public ResponseEntity<ImagenPayload> getInfoFotoActividad(@PathVariable Long idActividad, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver información de foto de actividad cargada");
		return fileStorageService.getInfoFotoActividad(idActividad);
	}
	
	@GetMapping("/actividad/info")
	public List<ImagenPayload> getInfoFotoActividads(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver información de fotos de actividades cargadas");
		return fileStorageService.getInfoFotosGeneric("actividad");
	}
	
	@PostMapping("/actividad/{id}")
	public ResponseEntity<?> uploadImageActividad(@PathVariable Long id, @RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Dar de alta/modificar una foto a actividad");
		String message = "";
		try {
			fileStorageService.saveFotoActividad(file, id);
			message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
	@DeleteMapping("/actividad/{idActividad}")
	public ResponseEntity<?> deleteFotoActividad(@PathVariable Long idActividad, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Eliminar una foto a actividad");
		String message = "";
		try {
			fileStorageService.deleteFotoActividad(idActividad);
			message = "Se ha eliminado exitosamente la foto ID: '" + idActividad +"'.";
		} catch (Exception e) {
			message = "No se pudo eliminar la foto ID: '" + idActividad + "'. Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	//Fin Actividad
	
	
	
	
	
	
	
	//Inicio ProgramaDeActividades
	@GetMapping("/programaDeActividades/search/{idProgramaDeActividades}")
	public ResponseEntity<byte[]> getFotoProgramaDeActividades(@PathVariable Long idProgramaDeActividades, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PROGRAMA_DE_ACTIVIDADES, "Ver foto de programa de actividades en tamaño completo");
		return fileStorageService.getFotoProgramaDeActividades(idProgramaDeActividades);
	}
	
	@GetMapping("/programaDeActividades/search_tabla/{idProgramaDeActividades}")
	public ResponseEntity<byte[]> getFotoTablaProgramaDeActividades(@PathVariable Long idProgramaDeActividades, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver foto de programa de actividades en tamaño tabla");
		return fileStorageService.getFotoTablaProgramaDeActividades(idProgramaDeActividades);
	}
	
	@GetMapping("/programaDeActividades/info/{idProgramaDeActividades}")
	public ResponseEntity<ImagenPayload> getInfoFotoProgramaDeActividades(@PathVariable Long idProgramaDeActividades, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver información de foto de programa de actividades cargada");
		return fileStorageService.getInfoFotoProgramaDeActividades(idProgramaDeActividades);
	}
	
	@GetMapping("/programaDeActividades/info")
	public List<ImagenPayload> getInfoFotoProgramaDeActividadess(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.ACTIVIDAD, "Ver información de fotos de programas de actividades cargadas");
		return fileStorageService.getInfoFotosGeneric("programaDeActividades");
	}
	
	@PostMapping("/programaDeActividades/{id}")
	public ResponseEntity<?> uploadImageProgramaDeActividades(@PathVariable Long id, @RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Dar de alta/modificar una foto a programa de actividades");
		String message = "";
		try {
			fileStorageService.saveFotoProgramaDeActividades(file, id);
			message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
	@DeleteMapping("/programaDeActividades/{idProgramaDeActividades}")
	public ResponseEntity<?> deleteFotoProgramaDeActividades(@PathVariable Long idProgramaDeActividades, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.ACTIVIDAD, "Eliminar una foto a programa de actividades");
		String message = "";
		try {
			fileStorageService.deleteFotoProgramaDeActividades(idProgramaDeActividades);
			message = "Se ha eliminado exitosamente la foto ID: '" + idProgramaDeActividades +"'.";
		} catch (Exception e) {
			message = "No se pudo eliminar la foto ID: '" + idProgramaDeActividades + "'. Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	//Fin ProgramaDeActividades
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	//Trash
	//No se utilizará en el proyecto, métodos obsoletos.
	/*@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Dar de alta una foto a contacto");
		String message = "";
		try {
			fileStorageService.save(file);
			 message = "Se ha cargado exitosamente la foto: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		//return message;
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
	@GetMapping("/buscar/{filename:.+}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		Resource file = fileStorageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@DeleteMapping("/{filename:.+}")
	public ResponseEntity<?> deleteImage(@PathVariable String filename) {
		try {
			boolean existed = fileStorageService.delete(filename);
			
			if (existed) {
				return ResponseEntity.ok().body(new ApiResponse(true, "Imágen borrada exitosamente: " + filename));
			} else {
				throw new BadRequestException("Algo salió mal en la baja. Es posible que la imágen no exista.");
			}
		} catch (Exception e) {
			throw new BadRequestException("No se pudo eliminar la imágen: " + filename + ". Error: " + e.getMessage());
		}
	}*/
	
	
}
