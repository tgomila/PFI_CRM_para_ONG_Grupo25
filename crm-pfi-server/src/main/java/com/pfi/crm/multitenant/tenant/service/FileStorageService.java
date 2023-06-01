package com.pfi.crm.multitenant.tenant.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.pfi.crm.controller.ImageController;
import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.tenant.payload.FileInfoPayload;
import com.pfi.crm.multitenant.tenant.payload.ImagenPayload;
import com.pfi.crm.security.UserPrincipal;

@Service
public class FileStorageService {
	private final Path root = Paths.get("./fileTenantDB");
	
	//public void init() {
	//	try {
	//		Files.createDirectories(root);
	//	} catch (IOException e) {
	//		throw new RuntimeException("No se pudo inicializar/crear la carpeta fileTenantDB!");
	//	}
	//}
	
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
		String nombre_file = file.getOriginalFilename();//producto_3.jpg
		return path_file(nombre_file);
	}
	private Path path_file(String filename) {
		String[] separacion = filename.split("_");
		if(separacion.length != 2) {
			throw new RuntimeException("Verifique el nombre del archivo, tiene que ser por ejemplo: producto_3.jpg");
		}
		String tenant_folder = DBContextHolder.getCurrentDb(); //tenant2
		String model_folder = separacion[0];		//producto
		String model_id_extension = separacion[1];	// 3.jpg
		
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
	
	//No creo que se use
	public Resource getFotoPerfilDescargar(UserPrincipal user) {
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
	
	//Este se va a usar
	public ResponseEntity<Resource> getFotoPerfilUrl(UserPrincipal user) {
		Resource file = this.getFotoPerfilDescargar(user);
		return convertirFotoAFront(file);
	}
	
	private ResponseEntity<Resource> convertirFotoAFront(Resource file) {
		//Este return lo devuelve para "ver como link".
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			String filename = file.getFilename();
			String contentDispositionValue;
			contentDispositionValue = String.format("inline; filename=\"%s\"; filename*=UTF-8''%s", 
			        filename, URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replace("+", "%20"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDispositionValue); // Establecer el encabezado de contenido en línea
			return ResponseEntity.ok().headers(headers).body(file);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new BadRequestException("Algo salió mal");
		
	}
	
	public ResponseEntity<byte[]> getFotoPerfil(UserPrincipal user) {
		return getFotoContacto(user.getIdContacto());
	}
	
	public ResponseEntity<byte[]> getFotoContacto(Long idContacto) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			Resource file;//resource

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			HttpHeaders headers = new HttpHeaders();
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				file =resource_jpg;
				headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			} else if(resource_png.exists() || resource_png.isReadable()) {
				file = resource_png;
				headers.setContentType(MediaType.IMAGE_PNG); // Establecer el tipo de contenido a imagen JPEG
			} else {
				String mensaje = "Foto no encontrada para el contacto con el id: " + idContacto;
		        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje.getBytes());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
			}
			
			//Lo convierto a 96x96px
			// Leer la imagen original
			BufferedImage originalImage = ImageIO.read(file.getFile());
			
			// Redimensionar la imagen a 96x96 píxeles
		    Image resizedImage = originalImage.getScaledInstance(96, 96, Image.SCALE_DEFAULT);
		    BufferedImage resizedBufferedImage = new BufferedImage(96, 96, BufferedImage.TYPE_INT_RGB);
		    resizedBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
		    
		    // Convertir la imagen redimensionada a un arreglo de bytes
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ImageIO.write(resizedBufferedImage, "jpeg", baos);
		    byte[] imageBytes = baos.toByteArray();
			
		    // Configurar las cabeceras de la respuesta
		    headers.setContentLength(imageBytes.length);
			String filename = file.getFilename();
			String contentDispositionValue;
			contentDispositionValue = String.format("inline; filename=\"%s\"; filename*=UTF-8''%s", 
			        filename, URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replace("+", "%20"));
			headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDispositionValue); // Establecer el encabezado de contenido en línea
			return ResponseEntity.ok().headers(headers).body(imageBytes);
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	//Mismo de arriba pero info
	public ResponseEntity<ImagenPayload> getInfoFotoPerfil(UserPrincipal user) {
		return getInfoFotoContacto(user.getIdContacto());
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoContacto(Long idContacto) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			LocalDateTime fecha_mas_tardia = null;
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFotoPerfil_jpg);
			} else if(resource_png.exists() || resource_png.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFotoPerfil_png);
			} else {
				String mensaje = "Foto no encontrada para el contacto con el id: " + idContacto;
		        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje.getBytes());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
			}
			
		    // Configurar las cabeceras de la respuesta
			ImagenPayload imagen = new ImagenPayload(idContacto, "contacto", fecha_mas_tardia);
			return ResponseEntity.ok().body(imagen);
			
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
	
	public List<FileInfoPayload> loadAllContactos() {
		List<FileInfoPayload> imageInfos = this.loadAll("contacto").map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
					path.getFileName().toString()).build().toString();
			LocalDateTime fecha_de_creacion = null;
			try {
				System.out.println(path.toString());
				BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
				fecha_de_creacion = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("No se pudo cargar los archivos!");
			}
			return new FileInfoPayload(filename, url, fecha_de_creacion);
		}).collect((Collectors.toList()));
		
		return imageInfos;
	}
	
	
	/**
	 * @param model_folder ejemplo "contacto"
	 * @return lista
	 */
	public Stream<Path> loadAll(String model_folder) {
		try {
			String tenant_folder = DBContextHolder.getCurrentDb();
			Path pathContacto = root.resolve(tenant_folder).resolve(model_folder);
			
			//filter quita path de carpeta "contacto", dejando solo "contacto_1.jpg", "contacto_2.jpg", etc...
			Stream<Path> contactos = Files.walk(pathContacto, 1).filter(path -> !path.equals(pathContacto)).map(pathContacto::relativize);
			return contactos;
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar los archivos!");
		}
	}
	

	
	
	/**
	 * @param model_folder ejemplo "contacto"
	 * @return lista
	 */
	public List<FileInfoPayload> loadAllWithFecha(String model_folder) {
		try {
			String tenant_folder = DBContextHolder.getCurrentDb();
			Path pathContacto = root.resolve(tenant_folder).resolve(model_folder);
			
			//filter quita path de carpeta "contacto", dejando solo "contacto_1.jpg", "contacto_2.jpg", etc...
			List<FileInfoPayload> contactos = Files.walk(pathContacto, 1)
					.filter(path -> !path.equals(pathContacto))
					//.map(pathContacto::relativize);
					.map(path -> {
						String filename = path.getFileName().toString();
						String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
								path.getFileName().toString()).build().toString();
						LocalDateTime fecha_mas_tardia = getFechaMasTardia(path);
						return new FileInfoPayload(filename, url, fecha_mas_tardia);
					})
					.filter(fileWithPathAndCreationDate -> fileWithPathAndCreationDate != null)
					.collect((Collectors.toList()));
			return contactos;
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar los archivos!");
		}
	}
	
	private LocalDateTime getFechaMasTardia(Path path) {
		try {
			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
			LocalDateTime fecha_de_creacion = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
			LocalDateTime fecha_de_modificacion = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault());
			LocalDateTime fecha_mas_tardia = fecha_de_modificacion;
			if(fecha_de_modificacion.isAfter(fecha_de_creacion))
				fecha_mas_tardia = fecha_de_modificacion;
			else
				fecha_mas_tardia = fecha_de_creacion;
			
			return fecha_mas_tardia;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
