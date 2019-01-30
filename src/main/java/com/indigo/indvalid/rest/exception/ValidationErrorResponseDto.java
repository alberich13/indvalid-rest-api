package com.indigo.indvalid.rest.exception;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ValidationErrorResponseDto extends ErrorResponseDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -827805399821129454L;
	
	@NotNull
	private List<FieldErrorDto> fieldErrors;
	
	public List<FieldErrorDto> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
