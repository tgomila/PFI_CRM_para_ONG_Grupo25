package com.pfi.crm.multitenant.tenant.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.security.UserPrincipal;

@Service
public class FileStorageService {
	private final Path root = Paths.get("./fileTenantDB");
	
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo inicializar/crear la carpeta fileTenantDB!");
		}
	}
	
	private void crearCarpeta(Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			if(path != null)
				throw new RuntimeException("No se pudo inicializar/crear la carpeta: " + path);
			else
				throw new RuntimeException("No se pudo inicializar/crear la carpeta!");
		}
	}
	
	public void save(MultipartFile file) {
		try {
			Path destino = this.path_file(file);
			crearCarpeta(destino.getParent());
			Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
		} catch(Exception e) {
			if(e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("Un archivo con ese nombre ya existe.");
			}
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Path path_file(MultipartFile file) {
		String nombre_file = file.getOriginalFilename();//tenant1_producto_3.jpg
		return path_file(nombre_file);
	}
	private Path path_file(String filename) {
		String[] separacion = filename.split("_");
		if(separacion.length != 3) {
			throw new RuntimeException("Verifique el nombre del archivo, tiene que ser por ejemplo: tenant1_producto_3.jpg");
		}
		String tenant_folder = separacion[0];		//tenant1
		String model_folder = separacion[1];		//producto
		String model_id_extension = separacion[2];	// 3.jpg
		
		String nuevo_nombre = model_folder + "_" + model_id_extension;
		Path destino = root.resolve(tenant_folder).resolve(model_folder).resolve(nuevo_nombre);
		return destino;
	}
	
	public boolean existeFotoPerfil(UserPrincipal user) {
		try {
			String tenantName = DBContextHolder.getCurrentDb();
			Long idContacto = user.getIdContacto();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			
			//Se prueba si existe en jpg o png
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				return true;
			} else if(resource_png.exists() || resource_png.isReadable()) {
				return true;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public Resource getFotoPerfil(UserPrincipal user) {
		try {
			String tenantName = DBContextHolder.getCurrentDb();
			Long idContacto = user.getIdContacto();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			
			//Se prueba si existe en jpg o png
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				return resource_jpg;
			} else if(resource_png.exists() || resource_png.isReadable()) {
				return resource_png;
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La foto de perfil no existe!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public void saveFotoPerfil(MultipartFile file, UserPrincipal user) {
		try {
			//Preparo su nombre "miFoto.jpg" a: "contacto_3.jpg"
			String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		    String extension = StringUtils.getFilenameExtension(originalFileName);
		    if(!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("png")) {
		    	throw new BadRequestException("Tiene que subir un archivo de tipo foto .jpg o .png");
		    }
		    String newFileName = "contacto_" + user.getIdContacto().toString() + "." + extension;
			
		    //Preparo su path
		    String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil = root.resolve(tenantName).resolve("contacto").resolve(newFileName);
			crearCarpeta(fileFotoPerfil.getParent());
			
			//Guardo el archivo
			Files.copy(file.getInputStream(), fileFotoPerfil, StandardCopyOption.REPLACE_EXISTING);
			
		} catch(Exception e) {
			if(e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("Un archivo con ese nombre ya existe.");
			}
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void deleteFotoPerfil(UserPrincipal user) {
		try {
			String tenantName = DBContextHolder.getCurrentDb();
			Long idContacto = user.getIdContacto();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			boolean existe_jpg = Files.deleteIfExists(fileFotoPerfil_jpg);
			boolean existe_png = Files.deleteIfExists(fileFotoPerfil_png);
			if(!existe_jpg && !existe_png) {//Si no pudo borrar ninguno de los 2 tipos de fotos.
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La foto de perfil que desea eliminar no existe!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	
	public Resource load(String filename) {
		try {
			Path file = this.path_file(filename);
			Resource resource = new UrlResource(file.toUri());
			
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("No se pudo leer el archivo!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public boolean delete(String filename) {
		try {
			Path file = this.path_file(filename);
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	//public void deleteAll() {
	//	FileSystemUtils.deleteRecursively(root.toFile());
	//}
	
	/**
	 * @param tenant_folder ejemplo "tenant2"
	 * @param model_folder ejemplo "contacto"
	 * @return lista
	 */
	public Stream<Path> loadAll(String tenant_folder, String model_folder) {
		try {
			Path pathContacto = root.resolve(tenant_folder).resolve(model_folder);
			Stream<Path> contactos = Files.walk(pathContacto, 1).filter(path -> !path.equals(pathContacto)).map(pathContacto::relativize);
			return contactos;
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar los archivos!");
		}
	}

}
