package com.pfi.crm.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/*
 * Spring security nos provee de @AuthenticationPrincipal para acceder al usuario autenticado.
 * Esta clase esta por si queremos quitar el security de dependencia, podemos hacerlo modificando aca.
 */

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
