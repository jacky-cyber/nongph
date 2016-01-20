package com.felix.nsf.exception;

public class UnPrivilegeException extends RuntimeException{

	private static final long serialVersionUID = -5267693216524348532L;

	public UnPrivilegeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnPrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public UnPrivilegeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnPrivilegeException(Throwable cause) {
		super(cause);
	}
}
