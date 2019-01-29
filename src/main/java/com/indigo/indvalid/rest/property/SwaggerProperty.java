package com.indigo.indvalid.rest.property;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="swagger.metadata")
@Data
@Validated
public class SwaggerProperty {

	@Data
	@Validated
	public static class Tag {
		@NotNull
		private String name;
		@NotNull
		private String description;
	}
	
	@Data
	@Validated
	public static class Contact {
		@NotNull
		private String name;
		@NotNull
		private String url;
		@NotNull
		private String email;
	}
	
	@Data
	@Validated
	public static class Config {
		@NotNull
		private String validator;
	}
	
	@NotNull
	private String regexPath;
	
	@NotNull
	private String pathMapping;
	
	@NotNull
	private Tag tag;
	
	@NotNull
	private Config config;
	
	@NotNull
	private String title;

	@NotNull
	private String description;

	@NotNull
	private String version;
	
	@NotNull
	private Contact contact;
	
}
