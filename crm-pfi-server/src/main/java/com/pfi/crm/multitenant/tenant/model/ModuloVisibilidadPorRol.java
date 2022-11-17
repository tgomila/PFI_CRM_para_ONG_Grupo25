package com.pfi.crm.multitenant.tenant.model;

import java.util.LinkedHashSet;
import java.util.Set;

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
import javax.persistence.UniqueConstraint;

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
	private Set<ModuloVisibilidadPorRolTipo> modulos = new LinkedHashSet<ModuloVisibilidadPorRolTipo>();
	
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

	public ModuloVisibilidadPorRol(Long id, Role role, Set<ModuloVisibilidadPorRolTipo> modulos) {
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

	public Set<ModuloVisibilidadPorRolTipo> getModulos() {
		return modulos;
	}

	public void setModulos(Set<ModuloVisibilidadPorRolTipo> modulos) {
		this.modulos = modulos;
	}
	
	
}
