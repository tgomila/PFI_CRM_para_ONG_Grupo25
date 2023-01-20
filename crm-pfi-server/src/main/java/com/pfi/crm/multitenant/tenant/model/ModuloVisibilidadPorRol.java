package com.pfi.crm.multitenant.tenant.model;

import java.util.ArrayList;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;

@Entity
@Table(name = "modulo_visibilidad_por_rol"//, uniqueConstraints = {
			//@UniqueConstraint(columnNames = {
			//	"roles"
			//})
//}
)
public class ModuloVisibilidadPorRol {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name="role", nullable=false)
	@OrderBy("name ASC")
	private Role role;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
	}

	public ModuloVisibilidadPorRol(Long id, Role role, List<ModuloVisibilidadPorRolTipo> modulos) {
		super();
		this.id = id;
		this.role = role;
		this.modulos = modulos;
	}
	
	public void agregarModulo(ModuloVisibilidadPorRolTipo modulo) {
		modulos.add(modulo);
	}
	
	public void agregarModulo(ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		modulos.add(new ModuloVisibilidadPorRolTipo(null, moduloEnum, tipoVisibilidad));
	}
	
	/**
	 * quitar un modulo item de la instancia.
	 * @param moduloEnum
	 * @return True si debe guardarse en BD, False si no es necesario
	 */
	public boolean quitarModulo(ModuloEnum moduloEnum) {
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
	}
	
	public ModuloPayload toPayload() {
		ModuloPayload payload = new ModuloPayload();
		payload.setRol(this.getRole().getRoleName().toString());
		payload.setItems(this.getModulos().stream().map(e -> e.toPayload()).collect(Collectors.toList()));
		return payload;
	}
	
	class myItemComparator implements Comparator<ModuloVisibilidadPorRolTipo>
	{
	   
	    public int compare(ModuloVisibilidadPorRolTipo s1, ModuloVisibilidadPorRolTipo s2)
	    {
	        return s1.getModuloEnum().getOrder()-s2.getModuloEnum().getOrder();
	    }
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
	}

	public List<ModuloVisibilidadPorRolTipo> getModulos() {
		return modulos;
	}

	public void setModulos(List<ModuloVisibilidadPorRolTipo> modulos) {
		this.modulos = modulos;
	}
	
	
}
