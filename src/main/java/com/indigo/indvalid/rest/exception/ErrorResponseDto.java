package com.indigo.indvalid.rest.exception;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class ErrorResponseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1918651081331058099L;
	
	@NotNull
	private String message;
	@NotNull
	private String cause;
	@NotNull
	private Date date;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
