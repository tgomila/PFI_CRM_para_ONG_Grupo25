package com.pfi.crm.multitenant.tenant.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Producto;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private ContactoService contactoService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
	
	public ProductoPayload getProductoById(@PathVariable Long id) {
		System.out.println("ID producto: " + id);
		return productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id)).toPayload();
	}
	
	public List<ProductoPayload> getProductos() {
		return productoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	//Lista
	/**
	 * Stock actual = 0
	 * @return Lista de productos sin stock.
	 */
	public List<ProductoPayload> getProductosSinStock() {
		return productoRepository.findProductosWithoutStock().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	/**
	 * Stock actual >0
	 * @return Lista de productos con stock.
	 */
	public List<ProductoPayload> getProductosConStock() {
		return productoRepository.findProductosWithStock().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	/**
	 * Stock actual < Stock minimo
	 * @return Lista de productos con bajo stock, requiere reposición.
	 */
	public List<ProductoPayload> getProductosConBajoStock() {
		return productoRepository.findProductosWithLowStock().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	/**
	 * Stock actual > Stock minimo
	 * @return Lista de productos con suficiente stock.
	 */
	public List<ProductoPayload> getProductosConSuficienteStock() {
		return productoRepository.findProductosWithEnoughStock().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	/**
	 * Precio/Costo para reponer stock a todos sus productos 
	 * @param payload
	 * @return
	 */
	public String calcularPrecioReposicion() {
		BigDecimal precio = BigDecimal.ZERO;
		BigDecimal aux = productoRepository.calcularPrecioReposicion();
		if(aux != null)
			precio = precio.add(aux);
		precio.setScale(2, RoundingMode.CEILING);
		String precioString = new DecimalFormat("0.00").format(precio);
		return precioString;
	}
	
	
	public ProductoPayload altaProducto(ProductoPayload payload) {
		payload.setId(null);
		return altaOModificarProducto(payload).toPayload();
		//return productoRepository.save(new Producto(payload)).toPayload();
	}
	
	public String bajaProducto(Long id) {
		Producto m = productoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Producto", "id", id));
		String message = "Se ha dado de baja al producto id: '" + id + "'";
		m.setEstadoActivo(false);
		if(m.getProveedor()!=null) {
			message += ", y desasociado a su proveedor id: '" + m.getProveedor().getId() + "'";
			m.setProveedor(null);
		}
		m = productoRepository.save(m);
		productoRepository.delete(m);
		
		//Una vez eliminado el producto, se elimina su foto si es que poseia
		boolean existeFoto = fileStorageService.deleteFotoGeneric(id, "producto");
		message += existeFoto ? ". Se ha dado de baja la foto del producto también" : "";
		
		return message;
	}
	
	public ProductoPayload modificarProducto(ProductoPayload payload) {
		return this.altaOModificarProducto(payload).toPayload();
		//return productoRepository.save(new Producto(payload)).toPayload();
	}
	
	
	
	
	private Producto altaOModificarProducto(ProductoPayload payload) {
		Producto productoModel = null;
		if(payload.getId() != null) {//Modificar
			productoModel = productoRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Producto", "id", payload.getId()));
			productoModel.modificar(payload);
		}
		if(productoModel == null){//Alta
			productoModel = new Producto(payload);
			productoModel.setEstadoActivo(true);
		}
		
		//Busco su proveedor
		Contacto proveedor = null;
		if(payload.getProveedor() != null) {
			if(payload.getProveedor().getId() != null) {//Agrego su proveedor
				proveedor = contactoService.getContactoModelById(payload.getProveedor().getId());
			}
			else {
				//Doy de alta su proveedor (Contacto)
				//proveedor = contactoService.altaContactoModel(payload.getProveedor());
				proveedor = null;//No doy de alta el 
			}
		}
		else {
			proveedor = null;
		}
		productoModel.setProveedor(proveedor);
		
		
		return productoRepository.save(productoModel);
	}
	
	public String quitarProveedorDeSusProductos(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		String message = "";
		List<Producto> productosDelProveedor = productoRepository.findByProveedor_Id(idContacto);
		if(!productosDelProveedor.isEmpty()) {
			message += "Se ha desasociado al contacto id '" +  idContacto + "' como proveedor de";
			if(productosDelProveedor.size()>1)
				message += " sus facturas id's: ";
			else
				message += " su factura id: ";
			for(int i=0; i<productosDelProveedor.size();i++) {
				message += productosDelProveedor.get(i).getId();
				if(i<productosDelProveedor.size()-1)//no sea ultimo
					message += ", ";
				productosDelProveedor.get(i).setProveedor(null);
			}
			productoRepository.saveAll(productosDelProveedor);
		}
		return message;
	}
	
	public boolean existeProducto(Long id) {
		return productoRepository.existsById(id);
	}
	
	
	
	
	
	
	
	
	
	public List<ProductoPayload> generar_30_productos() {
		List<ContactoPayload> proveedores = this.proveedorGenerator();
		List<ProductoPayload> productos = new ArrayList<>();
		if(proveedores != null) {
			for(int i=0; i<proveedores.size(); i++) {//Por cada proveedor generado...
				for(int j=0; j<10; j++) {			//...creo 10 productos
					ProductoPayload payload = new ProductoPayload();
					payload.setTipo("Random para gráfico");
					payload.setDescripcion("Producto nroº"+i*10+j);
					payload.setPrecioVenta(BigDecimal.valueOf(250.00));
					payload.setCantFijaCompra(10);
					payload.setCantMinimaStock(100);
					payload.setStockActual(100 - 40 + j*10);
					payload.setFragil(false);
					payload.setProveedor(proveedores.get(i));
					payload = this.altaProducto(payload);
					productos.add(payload);
				}
			}
		}
		return productos;
	}
	
	private List<ContactoPayload> proveedorGenerator() {
		final List<String> NOMBRE = Arrays.asList("Kiosco", "Supermercado", "Almacén");
		List<ContactoPayload> proveedores = new ArrayList<>();
		for(int i=0; i<NOMBRE.size(); i++) {
			ContactoPayload proveedor = contactoService.contactoGenerator();
			Random random = new Random();
			int nro_1_999_aleatorio = random.nextInt(999) + 1;
			proveedor.setNombreDescripcion(NOMBRE.get(random.nextInt(NOMBRE.size())) + " Nroº" + nro_1_999_aleatorio);
			proveedores.add(contactoService.altaContacto(proveedor));
		}
		return proveedores;
		
	}
	
	
	
}