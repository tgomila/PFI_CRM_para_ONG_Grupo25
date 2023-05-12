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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	private final Path root = Paths.get("./fileTenantDB");
	
	public void init() {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("No se pudo inicializar/crear la carpeta uploads!");
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
