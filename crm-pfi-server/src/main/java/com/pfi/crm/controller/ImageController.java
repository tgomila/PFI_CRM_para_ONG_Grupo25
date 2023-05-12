package com.pfi.crm.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.payload.FileInfoPayload;
import com.pfi.crm.multitenant.tenant.service.FileStorageService;
import com.pfi.crm.payload.response.ApiResponse;

@RestController
@RequestMapping("/api/images")
public class ImageController {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			fileStorageService.save(file);
			 message = "Uploaded the image successfully: " + file.getOriginalFilename();
		} catch (Exception e) {
			message = "No se pudo cargar el archivo: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
		}
		return message;
	}
	
	@GetMapping("/contacto/{tenant_name}")
	@PreAuthorize("hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public List<FileInfoPayload> getListImages(@PathVariable String tenant_name) {
		List<FileInfoPayload> imageInfos = fileStorageService.loadAll(tenant_name, "contacto").map(path -> {
			String filename = tenant_name + "_" + path.getFileName().toString();
			String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
					tenant_name + "_" + path.getFileName().toString()).build().toString();
			return new FileInfoPayload(filename, url);
		}).collect((Collectors.toList()));
		
		return imageInfos;
	}
	
	@GetMapping("/{filename:.+}")
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
