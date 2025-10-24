package com.tech.hispania.apigen.app.exceptions;

public class ApiGenException extends Exception {

	private static final long serialVersionUID = -5256168004611498012L;

	private int code;
	
	public ApiGenException(int code, String message) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "ApiGenException [code=" + code + ", message=" + super.getMessage() + "]";
	}
}
