package com.pfi.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import com.pfi.crm.multitenant.tenant.payload.FileInfoPayload;
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
	
	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.CONTACTO, "Dar de alta una foto a contacto");
		String message = "";
		try {
			fileStorageService.save(file);
			 message = "Uploaded the image successfully: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			throw new BadRequestException(message);
		}
		//return message;
		return ResponseEntity.ok().body(new ApiResponse(true, message));
	}
	
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
			 message = "Uploaded the image successfully: " + file.getOriginalFilename();
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
	
	@GetMapping("/contacto/search/{idContacto}")
	public ResponseEntity<byte[]> getFotoContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		//No requiere seguridad para ver su foto de perfil
		return fileStorageService.getFotoContacto(idContacto);
	}
	
	@GetMapping("/contacto/info")
	public List<FileInfoPayload> getListImages(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver fotos de contactos cargados");
		//return fileStorageService.loadAllContactos();
		return fileStorageService.loadAllWithFecha("contacto");
		
		//List<FileInfoPayload> imageInfos = fileStorageService.loadAll("contacto").map(path -> {
		//	String filename = path.getFileName().toString();
		//	String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
		//			path.getFileName().toString()).build().toString();
		//	return new FileInfoPayload(filename, url, null);
		//}).collect((Collectors.toList()));
		
		//return imageInfos;
	}
	
	@GetMapping("/contacto/info/{idContacto}")
	public ResponseEntity<ImagenPayload> getInfoFotoContacto(@PathVariable Long idContacto, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CONTACTO, "Ver fotos de contactos cargados");
		return fileStorageService.getInfoFotoContacto(idContacto);
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
	}
	
	
}
