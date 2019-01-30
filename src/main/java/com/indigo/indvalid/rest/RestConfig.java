package com.indigo.indvalid.rest;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.time.LocalDate;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.indigo.indvalid.rest.property.SwaggerProperty;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configurac&oacute;n de Swagger2 para habilitar la documentaci&oacute;n de servicios REST
 *
 */
@Slf4j
@Configuration
@ComponentScan
@EnableSwagger2
public class RestConfig {

	@Autowired
	private TypeResolver typeResolver;
	
	@Autowired
	private SwaggerProperty swaggerProperty;

	@Bean
	public Docket swaggerApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(paths()).build()
				.pathMapping(swaggerProperty.getPathMapping())
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(newRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.tags(new Tag(swaggerProperty.getTag().getName(), swaggerProperty.getTag().getDescription()));
	}

	@Bean
	@ConditionalOnProperty("swagger.ui.enabled")
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder()
				.displayOperationId(true)
				.displayRequestDuration(true)
				.validatorUrl(swaggerProperty.getConfig().getValidator())
				.operationsSorter(OperationsSorter.ALPHA)
				.showExtensions(true)
				.build();
	}
	 
	/**
	 * API Info
	 * @return Informaci&oacute;n del API
	 */
	private ApiInfo apiInfo() {
		log.info("{} version {} , paths {}",swaggerProperty.getTitle(),swaggerProperty.getVersion(), swaggerProperty.getRegexPath());
		return new ApiInfoBuilder().title(swaggerProperty.getTitle())
				.description(swaggerProperty.getDescription()).version(swaggerProperty.getVersion())
				.contact(new Contact(swaggerProperty.getContact().getName(), swaggerProperty.getContact().getUrl(), swaggerProperty.getContact().getEmail())).build();
	}

	/**
	 * Regex Path
	 * @return Predicate regex path
	 */
	private Predicate<String> paths() {
		return regex(swaggerProperty.getRegexPath());
	}
	
	
}
