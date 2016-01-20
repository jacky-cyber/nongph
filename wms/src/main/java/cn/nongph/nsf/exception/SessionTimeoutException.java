package com.felix.nsf.exception;

public class SessionTimeoutException extends RuntimeException{

	private static final long serialVersionUID = 6567489877805537058L;

	/**
	 * 
	 */
	public SessionTimeoutException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SessionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SessionTimeoutException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public SessionTimeoutException(Throwable cause) {
		super(cause);
	}
}
