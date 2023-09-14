package com.pfi.crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Cuando la API tira exceptions, le respondo al cliente
 * En este caso defino el ResponseStatus INTERNAL_SERVER_ERROR
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2700938025188400828L;

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
}
