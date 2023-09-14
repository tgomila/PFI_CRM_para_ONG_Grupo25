package com.pfi.crm.multitenant.tenant.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.pfi.crm.multitenant.tenant.model.Actividad;
import com.pfi.crm.multitenant.tenant.model.ProgramaDeActividades;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.FileInfoPayload;
import com.pfi.crm.multitenant.tenant.payload.ImagenPayload;
import com.pfi.crm.security.UserPrincipal;

@Service
public class FileStorageService {
	private final Path root = Paths.get("./fileTenantDB");
	
	@Autowired
	private ContactoService contactoService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ActividadService actividadService;
	
	@Autowired
	private ProgramaDeActividadesService programaDeActividadesService;

	
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
	
	/*public boolean existeFotoPerfil(UserPrincipal user) {
		try {
			String tenantName = DBContextHolder.getCurrentDb();
			Long idContacto = user.getIdContacto();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_jpeg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpeg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFotoPerfil_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			
			//Se prueba si existe en jpg o png
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				return true;
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				return true;
			} else if(resource_png.exists() || resource_png.isReadable()) {
				return true;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}*/
	
	//No creo que se use
	public Resource getFotoPerfilDescargar(UserPrincipal user) {
		try {
			String tenantName = DBContextHolder.getCurrentDb();
			Long idContacto = user.getIdContacto();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_jpeg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpeg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFotoPerfil_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			
			//Se prueba si existe en jpg o png
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				return resource_jpg;
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				return resource_jpeg;
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
	
	/*public ResponseEntity<byte[]> getFotoContacto(Long idContacto) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_jpeg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpeg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			System.out.println("----------------------------------------------------------------------");
			System.out.println(fileFotoPerfil_jpg);
			System.out.println(fileFotoPerfil_jpeg);
			System.out.println(fileFotoPerfil_png);
			System.out.println("----------------------------------------------------------------------");
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFotoPerfil_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			Resource file;//resource

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			HttpHeaders headers = new HttpHeaders();
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				file =resource_jpg;
				headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				file = resource_jpeg;
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
			//Image resizedImage = originalImage.getScaledInstance(96, 96, Image.SCALE_DEFAULT);
			//BufferedImage resizedBufferedImage = new BufferedImage(96, 96, BufferedImage.TYPE_INT_RGB);
			//resizedBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
			
			// Convertir la imagen redimensionada a un arreglo de bytes
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpeg", baos);
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
	}*/
	
	/*public ResponseEntity<byte[]> getFotoTablaContacto(Long idContacto) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_jpeg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpeg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFotoPerfil_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());
			Resource file;//resource

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			HttpHeaders headers = new HttpHeaders();
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				file =resource_jpg;
				headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				file = resource_jpeg;
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
	}*/
	
	public ResponseEntity<byte[]> getFotoPerfil(UserPrincipal user) {
		return getFotoContacto(user.getIdContacto());
	}
	
	public ResponseEntity<byte[]> getFotoContacto(Long idContacto) {
		return getFotoGeneric(idContacto, "contacto", false);
	}
	
	public ResponseEntity<byte[]> getFotoTablaContacto(Long idContacto) {
		return getFotoGeneric(idContacto, "contacto", true);
	}
	
	public ResponseEntity<byte[]> getFotoProducto(Long idProducto) {
		return getFotoGeneric(idProducto, "producto", false);
	}
	
	public ResponseEntity<byte[]> getFotoTablaProducto(Long idProducto) {
		return getFotoGeneric(idProducto, "producto", true);
	}
	
	public ResponseEntity<byte[]> getFotoActividad(Long idActividad) {
		return getFotoGeneric(idActividad, "actividad", false);
	}
	
	public ResponseEntity<byte[]> getFotoTablaActividad(Long idActividad) {
		return getFotoGeneric(idActividad, "actividad", true);
	}
	
	public ResponseEntity<byte[]> getFotoProgramaDeActividades(Long idProgramaDeActividades) {
		return getFotoGeneric(idProgramaDeActividades, "programaDeActividades", false);
	}
	
	public ResponseEntity<byte[]> getFotoTablaProgramaDeActividades(Long idProgramaDeActividades) {
		return getFotoGeneric(idProgramaDeActividades, "programaDeActividades", true);
	}
	
	public ResponseEntity<byte[]> getFotoGeneric(Long id, String nombreTipoArchivo, boolean isFotoForTable) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFoto_jpg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpg");
			Path fileFoto_jpeg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpeg");
			Path fileFoto_png = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFoto_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFoto_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFoto_png.toUri());
			Resource file;//resource

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			HttpHeaders headers = new HttpHeaders();
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				file =resource_jpg;
				headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				file = resource_jpeg;
				headers.setContentType(MediaType.IMAGE_JPEG); // Establecer el tipo de contenido a imagen JPEG
			} else if(resource_png.exists() || resource_png.isReadable()) {
				file = resource_png;
				headers.setContentType(MediaType.IMAGE_PNG); // Establecer el tipo de contenido a imagen JPEG
			} else {
				String mensaje = "Foto no encontrada para " + nombreTipoArchivo + " con el id: " + id;
				//return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje.getBytes());
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
			}
			
			//Lo convierto a 96x96px
			// Leer la imagen original
			BufferedImage originalImage = ImageIO.read(file.getFile());
			
			if(isFotoForTable) {// Redimensionar la imagen a 96x96 píxeles
				Image resizedImage = originalImage.getScaledInstance(96, 96, Image.SCALE_DEFAULT);
				BufferedImage resizedBufferedImage = new BufferedImage(96, 96, BufferedImage.TYPE_INT_RGB);
				resizedBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
				originalImage = resizedBufferedImage;
			}
			
			// Convertir la imagen redimensionada a un arreglo de bytes
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpeg", baos);
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
	
	/*public ResponseEntity<ImagenPayload> getInfoFotoContacto(Long idContacto) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFotoPerfil_jpg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpg");
			Path fileFotoPerfil_jpeg = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".jpeg");
			Path fileFotoPerfil_png = root.resolve(tenantName).resolve("contacto").resolve("contacto_" + idContacto.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFotoPerfil_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFotoPerfil_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFotoPerfil_png.toUri());

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			LocalDateTime fecha_mas_tardia = null;
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFotoPerfil_jpg);
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFotoPerfil_jpeg);
			} else if(resource_png.exists() || resource_png.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFotoPerfil_png);
			} else {
				//String mensaje = "Foto no encontrada para el contacto con el id: " + idContacto;
				ImagenPayload imagen = new ImagenPayload(idContacto, "contacto", null);
				return ResponseEntity.ok().body(imagen);
				//throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
			}
			
			// Configurar las cabeceras de la respuesta
			ImagenPayload imagen = new ImagenPayload(idContacto, "contacto", fecha_mas_tardia);
			return ResponseEntity.ok().body(imagen);
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}*/

	
	public ResponseEntity<ImagenPayload> getInfoFotoPerfil(UserPrincipal user) {
		return getInfoFotoGeneric(user.getIdContacto(), "contacto");
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoContacto(Long id) {
		return getInfoFotoGeneric(id, "contacto");
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoProducto(Long id) {
		return getInfoFotoGeneric(id, "producto");
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoActividad(Long id) {
		return getInfoFotoGeneric(id, "actividad");
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoProgramaDeActividades(Long id) {
		return getInfoFotoGeneric(id, "programaDeActividades");
	}
	
	public ResponseEntity<ImagenPayload> getInfoFotoGeneric(Long id, String nombreTipoArchivo) {
		try {
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFoto_jpg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpg");
			Path fileFoto_jpeg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpeg");
			Path fileFoto_png = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFoto_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFoto_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFoto_png.toUri());

			//Segundo, lo convierto en URL para front, sino se me descarga la foto y no es lo que quiero.
			//Se prueba si existe en jpg o png
			LocalDateTime fecha_mas_tardia = null;
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFoto_jpg);
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFoto_jpeg);
			} else if(resource_png.exists() || resource_png.isReadable()) {
				fecha_mas_tardia = getFechaMasTardia(fileFoto_png);
			} else {
				//String mensaje = "Foto no encontrada para el " + nombreTipoArchivo + " con el id: " + id;
				ImagenPayload imagen = new ImagenPayload(id, nombreTipoArchivo, null);
				return ResponseEntity.ok().body(imagen);
				//throw new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
			}
			
			// Configurar las cabeceras de la respuesta
			ImagenPayload imagen = new ImagenPayload(id, nombreTipoArchivo, fecha_mas_tardia);
			return ResponseEntity.ok().body(imagen);
			
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public void saveFotoPerfil(MultipartFile file, UserPrincipal user) {
		if(!contactoService.existeContacto(user.getIdContacto()))
			throw new BadRequestException("Error al guardar la imágen, el contacto con ID: '" + user.getIdContacto() + "' no existe en la base de datos.");
		saveFotoGeneric(file, user.getIdContacto(), "contacto");
	}
	
	public void saveFotoContacto(MultipartFile file, Long id) {
		if(!contactoService.existeContacto(id))
			throw new BadRequestException("Error al guardar la imágen, el contacto con ID: '" + id + "' no existe en la base de datos.");
		saveFotoGeneric(file, id, "contacto");
	}
	
	public void saveFotoProducto(MultipartFile file, Long id) {
		if(!productoService.existeProducto(id))
			throw new BadRequestException("Error al guardar la imágen, el producto con ID: '" + id + "' no existe en la base de datos.");
		saveFotoGeneric(file, id, "producto");
	}
	
	public void saveFotoActividad(MultipartFile file, Long id) {
		if(!actividadService.existeActividad(id))
			throw new BadRequestException("Error al guardar la imágen, la avtividad con ID: '" + id + "' no existe en la base de datos.");
		saveFotoGeneric(file, id, "actividad");
	}
	
	public void saveFotoProgramaDeActividades(MultipartFile file, Long id) {
		if(!programaDeActividadesService.existeProgramaDeActividades(id))
			throw new BadRequestException("Error al guardar la imágen, el programa de actividades con ID: '" + id + "' no existe en la base de datos.");
		saveFotoGeneric(file, id, "programaDeActividades");
		//Este va a ser particular, va a dar de alta la misma foto a todas sus actividades que no posean foto
		ProgramaDeActividades programa = programaDeActividadesService.getProgramaDeActividadesModelById(id);
		List<Actividad> actividadesDelPrograma = programa.getActividades();
		for(Actividad actividad: actividadesDelPrograma) {
			boolean noExisteFoto = getFotoPathGeneric(actividad.getId(), "programaDeActividades") == null;
			if(noExisteFoto) {
				saveFotoActividad(file, actividad.getId());
			}
		}
	}
	
	/**
	 * 
	 * @param file foto a guardar
	 * @param id id de la foto
	 * @param el_la se usa para mensajes en caso de error o success
	 * @param nombreTipoArchivo se usa para nombre archivo 'contacto_3', y mensajes
	 */
	private void saveFotoGeneric(MultipartFile file, Long id, String nombreTipoArchivo) {
		try {
			//Preparo su nombre "miFoto.jpg" a: "contacto_3.jpg"
			String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
			String extension = StringUtils.getFilenameExtension(originalFileName);
			if(!extension.equalsIgnoreCase("png") && !extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("jpeg")) {
				throw new BadRequestException("Tiene que subir un archivo de tipo foto .jpg, .jpeg o .png");
			}
			if(id == null || id.intValue() < 0) {
				throw new BadRequestException("Error al guardar la foto del " + nombreTipoArchivo + " sin id especificado");
			}
			if(nombreTipoArchivo == null || nombreTipoArchivo.isEmpty()) {
				throw new BadRequestException("Error interno al guardar el archivo, contacte al administrador. Error: nombreTipoArchivo/el_la == null");
			}
			String newFileName = nombreTipoArchivo + "_" + id.toString() + "." + extension;
			
			//Preparo su path
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFoto = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(newFileName);
			crearCarpeta(fileFoto.getParent());
			
			
			//borro si existe anteriormente otra foto, se usa esto porque si se da de alta png y ya existe jpg, no va a reemplazar existemte y va a haber 2 fotos
			deleteFotoGeneric(id, nombreTipoArchivo);
			
			//Guardo el archivo
			Files.copy(file.getInputStream(), fileFoto, StandardCopyOption.REPLACE_EXISTING);
			
		} catch(Exception e) {
			if(e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("Un archivo con ese nombre ya existe en carpeta " + nombreTipoArchivo);
			}
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void deleteFotoPerfil(UserPrincipal user) {
		deleteFotoGeneric(user.getIdContacto(), "contacto");
	}
	
	public void deleteFotoContacto(Long id) {
		deleteFotoGenericWithResponse(id, "contacto");
	}
	
	public void deleteFotoProducto(Long id) {
		deleteFotoGenericWithResponse(id, "producto");
	}
	
	public void deleteFotoActividad(Long id) {
		deleteFotoGenericWithResponse(id, "actividad");
	}
	
	public void deleteFotoProgramaDeActividades(Long id) {
		deleteFotoGenericWithResponse(id, "programaDeActividades");
	}
	
	private void deleteFotoGenericWithResponse(Long id, String nombreTipoArchivo) {
		boolean existe_foto = deleteFotoGeneric(id, nombreTipoArchivo);
		if(!existe_foto) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La foto de " + nombreTipoArchivo + ", ID:'" + id + "', que desea eliminar no existe!");
		}
	}
	
	public boolean deleteFotoGeneric(Long id, String nombreTipoArchivo) {
		try {
			Path fileFoto = getFotoPathGeneric(id, nombreTipoArchivo);//puede ser jpg, png, jpeg
			
			boolean existe_foto = fileFoto != null ? Files.deleteIfExists(fileFoto) : false;
			return existe_foto;
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	private Path getFotoPathGeneric(Long id, String nombreTipoArchivo) {
		try {
			if(id == null || id.intValue() < 0) {
				throw new BadRequestException("Error al obtener la foto del " + nombreTipoArchivo + " sin id especificado");
			}
			if(nombreTipoArchivo == null || nombreTipoArchivo.isEmpty()) {
				throw new BadRequestException("Error interno al obtener el archivo, contacte al administrador. Error: nombreTipoArchivo == null");
			}
			//Primero busco su foto de perfil (esto en formato "descarga, tomá el archivo").
			String tenantName = DBContextHolder.getCurrentDb();
			Path fileFoto_jpg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpg");
			Path fileFoto_jpeg = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".jpeg");
			Path fileFoto_png = root.resolve(tenantName).resolve(nombreTipoArchivo).resolve(nombreTipoArchivo + "_" + id.toString() + ".png");
			
			Resource resource_jpg = new UrlResource(fileFoto_jpg.toUri());
			Resource resource_jpeg = new UrlResource(fileFoto_jpeg.toUri());
			Resource resource_png = new UrlResource(fileFoto_png.toUri());
			
			System.out.println("----------------------------------------------------------------------");
			System.out.println(resource_jpg);
			System.out.println(resource_jpeg);
			System.out.println(resource_png);
			System.out.println("----------------------------------------------------------------------");
			
			//Se prueba si existe en jpg o png
			if (resource_jpg.exists() || resource_jpg.isReadable()) {
				System.out.println(resource_jpg);
				return fileFoto_jpg;
			} else if(resource_jpeg.exists() || resource_jpeg.isReadable()) {
				System.out.println(fileFoto_jpeg);
				return fileFoto_jpeg;
			} else if(resource_png.exists() || resource_png.isReadable()) {
				System.out.println(fileFoto_png);
				return fileFoto_png;
			}
			return null;
		} catch (MalformedURLException e) {
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
	
	//Probablemente no funciona
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
	public List<ImagenPayload> getInfoFotosGeneric(String model_folder) {
		try {
			String tenant_folder = DBContextHolder.getCurrentDb();
			Path pathContacto = root.resolve(tenant_folder).resolve(model_folder);
			
			//filter quita path de carpeta "contacto", dejando solo "contacto_1.jpg", "contacto_2.jpg", etc...
			List<ImagenPayload> contactos = Files.walk(pathContacto, 1)
					.filter(path -> !path.equals(pathContacto))
					.filter(path -> !Files.isDirectory(path)) // Filtrar solo archivos, no carpetas
					//.map(pathContacto::relativize);
					.map(path -> {
						String filename = path.getFileName().toString();
						Long id = obtenerNumero(filename);
						//String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", 
						//		path.getFileName().toString()).build().toString();
						LocalDateTime fecha_mas_tardia = getFechaMasTardia(path);
						return new ImagenPayload(id, model_folder,fecha_mas_tardia);
						//return new FileInfoPayload(filename, url, fecha_mas_tardia);
					})
					.filter(fileWithPathAndCreationDate -> fileWithPathAndCreationDate != null)
					.collect((Collectors.toList()));
			return contactos;
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar los archivos!");
		}
	}
	
	/**
	 * "contacto_1.jpg" obtendrías "1"
	 * @param nombreArchivo
	 * @return
	 */
	private Long obtenerNumero(String nombreArchivo) {
		Pattern patron = Pattern.compile("_(\\d+)\\.");
		Matcher matcher = patron.matcher(nombreArchivo);
		
		if (matcher.find()) {
			return Long.parseLong(matcher.group(1));
		}
		
		return null;
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
	

	/**
	 * NO USAR EN PRODUCCIÓN
	 * Solo para uso de testing en carga de datos ejemplo
	 * Lo que hace es copiar de fileTenantTest a fileTenantDB
	 * @param carpeta, por ejemplo "contacto"
	 */
	public void moveTestFilesToMainFolder(String carpeta) {
		String tenant_folder = DBContextHolder.getCurrentDb(); //tenant2
		//String model_folder = carpeta;		//producto
		//String model_id_extension = separacion[1];	// 3.jpg
		String mainDBPath = "fileTenantDB/" + tenant_folder + "/" + carpeta;
		String testPath = "fileTenantTest/" + tenant_folder + "/" + carpeta;
		
		try {
			deleteFilesInFolder(mainDBPath); // Elimina los archivos
			copyFilesToFolder(testPath, mainDBPath); // Copia los archivos
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFilesInFolder(String folderPath) throws IOException {
		File folder = new File(folderPath);
		FileUtils.cleanDirectory(folder);
	}
	
	private void copyFilesToFolder(String sourceFolderPath, String destinationFolderPath) throws IOException {
		File sourceFolder = new File(sourceFolderPath);
		File destinationFolder = new File(destinationFolderPath);
		FileUtils.copyDirectory(sourceFolder, destinationFolder);
	}
	
	/**
	 * NO USAR EN PRODUCCIÓN
	 * cargar DB fotos con fotos ejemplo. Una vez cargado los contactos
	 * @return boolean si fue el archivo copiado o no.
	 */
	public void cargarFotosContactoEjemploHaciaDB() {
		//Borrar fotos anteriores
		String tenant_folder = DBContextHolder.getCurrentDb(); //tenant2
		String mainDBPath = "fileTenantDB/" + tenant_folder + "/contacto";
		try {
			deleteFilesInFolder(mainDBPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Cargar nuevas fotos de ejemplo
		List<ContactoPayload> contactos = contactoService.getContactos();
		for(ContactoPayload contacto: contactos) {
			cargarFotosEjemploHaciaDB(contacto.getId(), contacto.getCuit(), "contacto");
		}
	}
	
	
	/**
	 * Busca el archivo en test folder y lo copia a la BD
	 * @param id del contacto
	 * @param textoSearch ejemplo cuit, para buscar el archivo de contacto
	 * @param nombreTipoArchivo
	 * @return boolean si fue el archivo copiado o no.
	 */
	public boolean cargarFotosEjemploHaciaDB(Long id, String textoSearch, String nombreTipoArchivo) {
		boolean isArchivoCopiado = false;
		if(id == null || textoSearch == null || nombreTipoArchivo == null
			|| (textoSearch != null && textoSearch.isEmpty())
			|| (nombreTipoArchivo != null && nombreTipoArchivo.isEmpty())) {
			return isArchivoCopiado;
		}
		String tenant_folder = DBContextHolder.getCurrentDb(); //tenant2
		
		String mainFolderDBPath = "fileTenantDB/" + tenant_folder + "/" + nombreTipoArchivo; //DB a donde mover las fotos de ejemplo
		String testFolderDBPath = "fileTenantTest/" + tenant_folder + "/" + nombreTipoArchivo;//Las fotos de ejemplo
		
		File testFolder = new File(testFolderDBPath);
		File[] files = testFolder.listFiles();
		
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.getName().contains(textoSearch)) {
					
					String extension = getFileExtension(file.getName());
					String newFileName = nombreTipoArchivo + "_" + id.toString() + "." + extension;
					try {
						Path sourcePath = file.toPath();
						Path destinationPath = new File(mainFolderDBPath, newFileName).toPath();
						Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						isArchivoCopiado= true;
					} catch (IOException e) {
						e.printStackTrace();
					}
					// Solo copiar el primer archivo encontrado, si existen más, no hacer nada.
					break;
				}
			}
		}
		return isArchivoCopiado;
	}

	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(dotIndex + 1);
		}
		return "";
	}

}
