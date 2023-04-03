package com.pfi.crm.multitenant.tenant.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;

@Entity
@Table(name = "modulo_visibilidad_por_rol"//, uniqueConstraints = {
			//@UniqueConstraint(columnNames = {
			//	"roles"
			//})
//}
)
public class ModuloVisibilidadPorRol {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuloVisibilidadPorRol.class);
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name="role", nullable=false)
	@OrderBy("name ASC")
	private Role role;
	
	//Antes era ManyToMany
	@OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval=true)
	@JoinTable(name = "modulo_visibilidad_por_rol_join_tipo",
			joinColumns = @JoinColumn(name = "modulo_visibilidad_por_rol_id"),
			inverseJoinColumns = @JoinColumn(name = "modulo_tipo_visibilidad_por_rol_id"))
	private List<ModuloVisibilidadPorRolTipo> modulos = new ArrayList<ModuloVisibilidadPorRolTipo>();
	
	//@ElementCollection
	//@MapKeyColumn(name = "modulo_enum")
	//@MapKeyEnumerated(EnumType.STRING)
	//private HashMap <ModuloEnum, ModuloTipoVisibilidadEnum> modulos = new HashMap<ModuloEnum, ModuloTipoVisibilidadEnum>();
	
	//@ElementCollection(targetClass = ModuloTipoVisibilidadEnum.class) 
	//@CollectionTable(name = "modulo_visible_por_rol_tipo_visibilidad", joinColumns = @JoinColumn(name = "modulo_id")) 
	//@Enumerated(EnumType.STRING) 
	//@Column(name = "tipo_visibilidad_name") 
	//private Set <ModuloTipoVisibilidadEnum> modulosTipoVisibilidadEnum;

	public ModuloVisibilidadPorRol() {
		super();
		agregarTodosLosModulosTipoOrdenadoYNoRepetido(); //No lo puedo ejecutar si no hay rol
	}

	public ModuloVisibilidadPorRol(Role role) {
		super();
		this.setRole(role);
		//agregarTodosLosModulosTipo();//ya lo hace setRole
	}

	/*public ModuloVisibilidadPorRol(Long id, Role role, List<ModuloVisibilidadPorRolTipo> modulos) {
		super();
		this.setId(id);
		this.setModulos(modulos);
		this.setRole(role);
		//agregarTodosLosModulos();//ya lo hace setRole
	}*/
	
	/**
	 * 
	 * @return boolean true si se modificó algo, false si no se ha hecho modificaciones.
	 */
	public boolean agregarTodosLosModulosTipoOrdenadoYNoRepetido() {
		//Si modulo es paid, será Sin_Suscripcion por default, o No_Vista si el modulo es free. 
		return agregarTodosLosModulosTipoOrdenadoYNoRepetido(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION);
	}
	
	/**
	 * - Se agregarán en orden del ModuloEnum.
	 * - Favor de ejecutar si y solo si, la suscripción esté activa/pago realizado.
	 * - La edición completa se la suele utilizar solo en rol admin
	 * @return boolean true si se modificó algo, false si no se ha hecho modificaciones.
	 */
	@SuppressWarnings("unused")
	private boolean agregarTodosLosModulosTipoAdminOrdenadoYNoRepetido() {
		return agregarTodosLosModulosTipoOrdenadoYNoRepetido(ModuloTipoVisibilidadEnum.EDITAR);
	}
	
	/**
	 * Solo utilizar este método con SOLO_VISTA o EDITAR si es premium en absolutamente todo.
	 * @param visibilidad
	 * @return boolean true si se modificó algo, false si no se ha hecho modificaciones.
	 */
	private boolean agregarTodosLosModulosTipoOrdenadoYNoRepetido(ModuloTipoVisibilidadEnum visibilidad) {
		boolean seAgregaronOModificaronModulos = false;
		boolean auxSeAgregaronOModificaronModulos = false;
		if(this.modulos == null) {
			modulos = new ArrayList<ModuloVisibilidadPorRolTipo>();
			seAgregaronOModificaronModulos = true;
		}
		List<ModuloVisibilidadPorRolTipo> modulosOrdenados = new ArrayList<ModuloVisibilidadPorRolTipo>();
		for(ModuloEnum itemOfAllModuloEnum: ModuloEnum.values()) {
			ModuloVisibilidadPorRolTipo aux = null;
			//Busco si existe, de ser así, lo agrego al aux
			for(ModuloVisibilidadPorRolTipo modulo: modulos) {
				if(modulo.getModuloEnum().equals(itemOfAllModuloEnum)) {
					aux = modulo;
					break;//Permito que no se repita al ser 1 solo aux.
				}
			}
			//Si existe, no cambio su visibilidad, solo corroboro que esté ok usando su método.
			if(aux != null) {
				auxSeAgregaronOModificaronModulos = aux.setTipoVisibilidad(aux.getTipoVisibilidad());
			}
			//Si no existe, creo un módulo nuevo.
			else {
				auxSeAgregaronOModificaronModulos = true;
				//Si es admin y market, entonces edita market
				if(role != null && role.getRoleName() != null && role.getRoleName().equals(RoleName.ROLE_ADMIN) && itemOfAllModuloEnum.equals(ModuloEnum.MARKETPLACE))
					aux = new ModuloVisibilidadPorRolTipo(itemOfAllModuloEnum, ModuloTipoVisibilidadEnum.EDITAR);
				else if(role != null && role.getRoleName() != null && !role.getRoleName().equals(RoleName.ROLE_ADMIN) && itemOfAllModuloEnum.equals(ModuloEnum.MARKETPLACE))
					aux = new ModuloVisibilidadPorRolTipo(itemOfAllModuloEnum, ModuloTipoVisibilidadEnum.NO_VISTA);//Si no es admin y es Market.
				else //Si no es market o market/admin, es visibilidad input. Si visibilidad es Sin_Suscripción pero modulo es free, se cambia a No_Vista. 
					aux = new ModuloVisibilidadPorRolTipo(itemOfAllModuloEnum, visibilidad);
			}
			modulosOrdenados.add(aux);
			if(auxSeAgregaronOModificaronModulos) {
				seAgregaronOModificaronModulos = true;
			}
		}
		this.modulos = modulosOrdenados;
		//desuscribir();
		return seAgregaronOModificaronModulos;//Puede ser true o false
	}
	
	
	public void suscribir() {
		for(ModuloVisibilidadPorRolTipo modulo: modulos) {
			//Te suscribo a todo lo pago
			modulo.suscribir();
			//Solo admin tiene acceso a Market, por si quiere suscribirse devuelta
			if(modulo.getModuloEnum().equals(ModuloEnum.MARKETPLACE)) {
				if(this.role.getRoleName().equals(RoleName.ROLE_ADMIN))
					modulo.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR);
				else
					modulo.setTipoVisibilidad(ModuloTipoVisibilidadEnum.NO_VISTA);
			}
		}
	}
	
	/**
	 * Requiere que haya rol cargado
	 */
	public void desuscribir() {
		for(ModuloVisibilidadPorRolTipo modulo: modulos) {
			//Te desuscribo a todo lo pago
			if(modulo.getModuloEnum().isPaidModule())
				modulo.setTipoVisibilidad(ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION);
			//Solo admin tiene acceso a Market, por si quiere suscribirse devuelta
			if(modulo.getModuloEnum().equals(ModuloEnum.MARKETPLACE) && role != null) {
				if(this.role.getRoleName().equals(RoleName.ROLE_ADMIN))
					modulo.setTipoVisibilidad(ModuloTipoVisibilidadEnum.EDITAR);
				else
					modulo.setTipoVisibilidad(ModuloTipoVisibilidadEnum.NO_VISTA);
			}
		}
	}
	
	public void suscribir(ModuloEnum moduloEnum) {
		for(ModuloVisibilidadPorRolTipo moduloTipo: modulos) {
			if(moduloTipo.getModuloEnum().equals(moduloEnum)) {
				moduloTipo.suscribir();
				break;
			}
		}
	}
	
	public void desuscribir(ModuloEnum moduloEnum) {
		for(ModuloVisibilidadPorRolTipo moduloTipo: modulos) {
			if(moduloTipo.getModuloEnum().equals(moduloEnum)) {
				moduloTipo.desuscribir();
			}
		}
	}
	
	public void cambiarTipoVisibilidad(ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		for(ModuloVisibilidadPorRolTipo moduloTipo: modulos) {
			if(moduloTipo.getModuloEnum().equals(moduloEnum)) {
				moduloTipo.setTipoVisibilidad(tipoVisibilidad);
			}
		}
	}
	
	
	public ModuloPayload toPayload() {
		ModuloPayload payload = new ModuloPayload();
		payload.setRol(this.getRole().getRoleName().toString());
		payload.setItems(this.getModulos().stream().map(e -> e.toPayload()).collect(Collectors.toList()));
		return payload;
	}
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		agregarTodosLosModulosTipoOrdenadoYNoRepetido(); //Admin no puede tener todo Editar, porque puede tener cosas no suscriptas o no haya pagado
		
		//if(role.getRoleName().equals(RoleName.ROLE_ADMIN))
		//	agregarTodosLosModulosTipoAdminOrdenadoYNoRepetido();
		//else
		//	agregarTodosLosModulosTipoOrdenadoYNoRepetido();
		//corroborarMarketPlaceSoloParaElAdmin();
	}

	public List<ModuloVisibilidadPorRolTipo> getModulos() {
		return modulos;
	}

	public void setModulos(List<ModuloVisibilidadPorRolTipo> modulos) {
		this.modulos = modulos;
		
		//Este caso es cuando voy a dar de baja este objeto, antes de hacerlo hago null la
		//	lista para eliminarla en repository uno x uno.
		if(modulos == null)
			return;
		
		agregarTodosLosModulosTipoOrdenadoYNoRepetido();
		//corroborarNoHayModuloEnumRepetido();
		//ordenarYCrearFaltantes();
		//corroborarMarketPlaceSoloParaElAdmin();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Sector basura de código.
	
	//Métodos reemplazados por agregarTodosLosModulosTipoOrdenadoYNoRepetido()
	@SuppressWarnings("unused")
	private void ordenarYCrearFaltantes() {
		List<ModuloVisibilidadPorRolTipo> modulosOrdenados = new ArrayList<ModuloVisibilidadPorRolTipo>();
		for(ModuloEnum itemOfAllModuloEnum: ModuloEnum.values()) {
			ModuloVisibilidadPorRolTipo aux = null;
			//Busco si existe, de ser así, lo agrego al aux
			for(ModuloVisibilidadPorRolTipo modulo: modulos) {
				if(modulo.getModuloEnum().equals(itemOfAllModuloEnum)) {
					aux = modulo;
					break;
				}
			}
			//Si existe, no cambio su visibilidad, solo corroboro que esté ok usando su método.
			if(aux != null)
				aux.setTipoVisibilidad(aux.getTipoVisibilidad());
			//Si no existe, creo un módulo nuevo.
			else
				aux = new ModuloVisibilidadPorRolTipo(itemOfAllModuloEnum, ModuloTipoVisibilidadEnum.SIN_SUSCRIPCION);
			modulosOrdenados.add(aux);
			
		}
		this.modulos = modulosOrdenados;
	}
	
	@SuppressWarnings("unused")
	private void corroborarMarketPlaceSoloParaElAdmin() {
		if(role == null || modulos == null)
			return;
		//No permito que el MarketPlace lo puedan ver otros roles, excepto admin.
		ModuloTipoVisibilidadEnum aux = ModuloTipoVisibilidadEnum.NO_VISTA;
		if(this.role.getRoleName().equals(RoleName.ROLE_ADMIN))	//Si es admin
			aux = ModuloTipoVisibilidadEnum.EDITAR;				//Seteo que puede editar
		else													//Si no es admin
			aux = ModuloTipoVisibilidadEnum.NO_VISTA;			//Seteo que no puede verlo
		
		for(ModuloVisibilidadPorRolTipo modulo: modulos) {				//Busco marketplace
			if(modulo.getModuloEnum().equals(ModuloEnum.MARKETPLACE)) {	//...si posee marketplace
				modulo.setTipoVisibilidad(aux);							//...Seteo que puede editar o no ver, dependiendo si es o no admin.
				break;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private boolean corroborarNoHayModuloEnumRepetido() {
		List<ModuloEnum> allModulos = Arrays.asList(ModuloEnum.values());
		List<ModuloVisibilidadPorRolTipo> modulosRepetidos = new ArrayList<ModuloVisibilidadPorRolTipo>();
		for(ModuloEnum moduleEnumToCheck: allModulos) {				//Elijo un móduloEnum
			int i = 0;
			for(ModuloVisibilidadPorRolTipo modulo: modulos) {			//Veo si esta repetido
				if(modulo.getModuloEnum().equals(moduleEnumToCheck)) {
					i++;
					if(i>1)												//Si está repetido
						modulosRepetidos.add(modulo);					//...lo agrego a lista a remover
				}
			}
		}
		if(!modulosRepetidos.isEmpty()) {				//Remover los repetidos
			modulos.removeAll(modulosRepetidos);
			modulosRepetidos.forEach(mrep -> LOGGER.info("ModuloTipo repetido: " + mrep.getModuloEnum().toString() + " ha sido quitado."));
			return true;	//Hay repetidos y fueron removidos, es solo para avisar.
		}
		else {
			return false;	//No hay repetidos
		}
	}
	//Fin métodos reemplazados por agregarTodosLosModulosTipoOrdenadoYNoRepetido().
	
	
	
	
	
	//Antiguamente iba a agregarse y quitarse módulos si uno no estaba suscripto.
	
	@SuppressWarnings("unused")
	private List<ModuloEnum> modulosNoAgregados() {
		List<ModuloEnum> allModulos = Arrays.asList(ModuloEnum.values());
		List<ModuloEnum> modulosAgregados = modulosAgregados();
		allModulos.removeAll(modulosAgregados);	//A partir de aquí allModulos es "modulosNoAgregados".
		return allModulos;
	}
	
	private List<ModuloEnum> modulosAgregados() {
		return modulos.stream().map(m -> m.getModuloEnum()).collect(Collectors.toList());
	}
	
	
	
	/**
	 * quitar un modulo item de la instancia.
	 * @param moduloEnum
	 * @return True si debe guardarse en BD, False si no es necesario
	 */
	/*public boolean quitarModulo(ModuloEnum moduloEnum) {
		List<ModuloVisibilidadPorRolTipo> aux = new ArrayList<ModuloVisibilidadPorRolTipo>();
		boolean mereceGuardarseEnBD = false;
		for(ModuloVisibilidadPorRolTipo modulo: modulos) {
			if(!modulo.getModuloEnum().equals(moduloEnum))
				aux.add(modulo);
			else
				mereceGuardarseEnBD = true;
		}
		this.setModulos(aux);
		return mereceGuardarseEnBD;
	}*/
	
	/**
	 * Crear módulo o editar visibilidad del módulo creado
	 * @param modulo
	 */
	/*public void agregarModulo(ModuloVisibilidadPorRolTipo modulo) {
		if(modulo.getModuloEnum() == null)
			throw new BadRequestException("El módulo enum ingresado es null");
		for(ModuloVisibilidadPorRolTipo oldModulo: modulos) {
			if(oldModulo.getModuloEnum() == null)
				throw new BadRequestException("Verifique id:" + oldModulo.getId() + " de ModuloVisibilidadPorRolTipo.java en la BD ya que moduloEnum es null.");
			if(oldModulo.getModuloEnum().equals(modulo.getModuloEnum())) {
				//Si entré acá, significa que existe y es modificar su visibilidad
				oldModulo.setTipoVisibilidad(modulo.getTipoVisibilidad());
				return; //Termino este método aquí.
			}
		}
		//Si llegue hasta acá, significa agregar nuevo porque no existe.
		modulos.add(modulo);
	}
	
	public void agregarModulo(ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		agregarModulo(new ModuloVisibilidadPorRolTipo(null, moduloEnum, tipoVisibilidad));
	}*/
	

	
	class myItemComparator implements Comparator<ModuloVisibilidadPorRolTipo>
	{
	   
	    public int compare(ModuloVisibilidadPorRolTipo s1, ModuloVisibilidadPorRolTipo s2)
	    {
	        return s1.getModuloEnum().getOrder()-s2.getModuloEnum().getOrder();
	    }
	}
}
