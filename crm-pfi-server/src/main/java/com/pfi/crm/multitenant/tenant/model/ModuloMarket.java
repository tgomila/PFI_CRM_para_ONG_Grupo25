package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDateTime;
import java.time.Period;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.payload.ModuloMarketPayload;

@Entity
@Table(name = "modulo_market")
public class ModuloMarket {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	//@NaturalId
	//@Column(length = 60)
	private ModuloEnum moduloEnum;
	
	private boolean prueba7DiasUtilizada;
	private LocalDateTime fechaPrueba7DiasUtilizada;
	
	//Null significa que nunca se ha suscripto.
	private LocalDateTime fechaMaximaSuscripcion;
	
	//Este boolean no es altamente necesario, pero permite saber si esta vencido (por fecha) pero la suscripcion esta
	//  activa, para el evento crónológico a las 00hs, cambiar la visibilidad de los roles a que no vean este módulo.
	//  Luego finalmente cambiar suscripción activa a false, con eso saber que no hay que cambiar la visibilidad de roles. 
	private boolean suscripcionActiva;
	
	public ModuloMarket() {
		super();
	}

	public ModuloMarket(ModuloEnum moduloEnum) {
		super();
		this.setId(null);
		this.moduloEnum = moduloEnum;
		this.prueba7DiasUtilizada = false;
		this.fechaPrueba7DiasUtilizada = null;
		this.fechaMaximaSuscripcion = null;// LocalDate.of(2000, 1, 1); //Se inicia con Una fecha vencida.
		this.suscripcionActiva = false;
	}
	
	public ModuloMarket(ModuloMarketPayload p) {
		super();
		this.setId(p.getId());
		this.setModuloEnum(p.getModuloEnum());
		this.setPrueba7DiasUtilizada(p.isPrueba7DiasUtilizada());
		this.setFechaMaximaSuscripcion(this.getFechaMaximaSuscripcion());
		this.setSuscripcionActiva(p.isSuscripcionActiva());
	}
	
	//Métodos sobre fecha de suscripción.
	public LocalDateTime sumarUnMes() {
		Period oneMonth = Period.ofMonths(1);
		return sumarPeriodoASuscripcion(oneMonth);
	}
	
	public LocalDateTime sumarUnAnio() {
		Period oneYear = Period.ofYears(1);
		return sumarPeriodoASuscripcion(oneYear);
	}
	
	private LocalDateTime sumarPeriodoASuscripcion(Period plusTime) {
		if(moduloEnum.isFreeModule())
			throw new BadRequestException("No se puede suscribir a un módulo gratuito");//return null;
		if(fechaMaximaSuscripcion == null || fechaMaximaSuscripcion.isBefore(LocalDateTime.now())) {
			fechaMaximaSuscripcion = LocalDateTime.now().plus(plusTime);
		}
		else {
			fechaMaximaSuscripcion = fechaMaximaSuscripcion.plus(plusTime);
		}
		this.suscripcionActiva = true;
		return fechaMaximaSuscripcion;
	}
	
	public boolean activarSieteDiasGratis() {
		if(moduloEnum.isFreeModule() || prueba7DiasUtilizada)
			return false;
		fechaPrueba7DiasUtilizada = LocalDateTime.now();
		if(fechaMaximaSuscripcion == null || fechaMaximaSuscripcion.isBefore(LocalDateTime.now())) {
			fechaMaximaSuscripcion = LocalDateTime.now().plusDays(7).plusHours(1);
		}
		else {
			fechaMaximaSuscripcion = fechaMaximaSuscripcion.plusDays(7).plusHours(1);
		}
		prueba7DiasUtilizada = true;
		this.suscripcionActiva = true;
		return prueba7DiasUtilizada;
	}
	
	public boolean poseeSuscripcionActiva() {
		return !poseeSuscripcionVencida();
	}
	
	public boolean poseeSuscripcionVencida() {
		if(moduloEnum.isFreeModule())
			return false;
		if(fechaMaximaSuscripcion == null)
			return true; //null significa nunca se ha suscripto
		return !fechaMaximaSuscripcion.isAfter(LocalDateTime.now());
	}
	
	public boolean isPaidModule() {
		return !moduloEnum.isFreeModule();
	}
	
	public boolean isFreeModule() {
		return moduloEnum.isFreeModule();
	}
	
	//Getters and Setters
	public ModuloEnum getModuloEnum() {
		return moduloEnum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setModuloEnum(ModuloEnum moduloEnum) {
		this.moduloEnum = moduloEnum;
	}

	public boolean isPrueba7DiasUtilizada() {
		return prueba7DiasUtilizada;
	}

	public void setPrueba7DiasUtilizada(boolean prueba7DiasUtilizada) {
		this.prueba7DiasUtilizada = prueba7DiasUtilizada;
	}

	public LocalDateTime getFechaPrueba7DiasUtilizada() {
		return fechaPrueba7DiasUtilizada;
	}

	public void setFechaPrueba7DiasUtilizada(LocalDateTime fechaPrueba7DiasUtilizada) {
		this.fechaPrueba7DiasUtilizada = fechaPrueba7DiasUtilizada;
	}

	public LocalDateTime getFechaMaximaSuscripcion() {
		return fechaMaximaSuscripcion;
	}

	public void setFechaMaximaSuscripcion(LocalDateTime fechaMaximaSuscripcion) {
		this.fechaMaximaSuscripcion = fechaMaximaSuscripcion;
	}

	public boolean isSuscripcionActiva() {
		return suscripcionActiva;
	}

	public void setSuscripcionActiva(boolean suscripcionActiva) {
		this.suscripcionActiva = suscripcionActiva;
	}
	
	public ModuloMarketPayload toPayload() {
		ModuloMarketPayload p = new ModuloMarketPayload();
		p.setId(this.getId());
		p.setModuloEnum(this.getModuloEnum());
		p.setPrueba7DiasUtilizada(this.isPrueba7DiasUtilizada());
		p.setFechaPrueba7DiasUtilizada(this.getFechaPrueba7DiasUtilizada());
		p.setFechaMaximaSuscripcion(this.getFechaMaximaSuscripcion());
		p.setSuscripcionActiva(this.isSuscripcionActiva());
		return p;
	}
	
	
}
