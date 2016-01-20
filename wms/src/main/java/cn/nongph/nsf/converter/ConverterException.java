package com.felix.nsf.converter;

public class ConverterException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ConverterException(String message, Throwable cause){
		super( message, cause );
	}
}
