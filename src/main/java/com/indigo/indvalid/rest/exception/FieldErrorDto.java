package com.indigo.indvalid.rest.exception;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class FieldErrorDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7024987785726045627L;
	
	@NotNull
	private String field;
	@NotNull
	private String error;
	
	public FieldErrorDto(String field, String error) {
		super();
		this.field = field;
		this.error = error;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
