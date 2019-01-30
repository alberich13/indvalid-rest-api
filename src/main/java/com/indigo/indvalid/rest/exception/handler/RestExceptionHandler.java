package com.indigo.indvalid.rest.exception.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.indigo.indvalid.rest.exception.ErrorResponseDto;
import com.indigo.indvalid.rest.exception.FieldErrorDto;
import com.indigo.indvalid.rest.exception.ValidationErrorResponseDto;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller Advice, manejo de excepciones con <code>ExceptionHandler</code> comunes a los servicios REST.
 * <ol>
 * <li>Errores de validaci&oacute;n de campos</li>
 * <li>Violaciones de restricciones</li>
 * <li>Tipo de datos incorrectos</li>
 * <li>Errores no identificados</li>
 * </ol>
 *
 */
@ControllerAdvice(basePackages = {"com.indigo.indvalid.rest.controller"} )
@Slf4j
public class RestExceptionHandler {
	
	/**
	 * Mensaje cuando ocurre un error en la validacio&oacute;n de campos
	 */
	@Value("${indvalid.rest.error.validation}")
	protected String validationError;
	
	/**
	 * Mensaje cuando ocurre un error en las restricciones de los par&aacute;metros
	 */
	@Value("${indvalid.rest.error.constraint}")
	protected String constraintError;

	/**
	 * Mensaje cuando ocurre un error interno en la aplicaci&oacute;n
	 */
	@Value("${indvalid.rest.error.internalError}")
	protected String internalError;
	
	/**
	 * Handler MethodArgumentNotValidException utilizado para validaciones de campos de entrada
	 * @param ex Excepci&oacute;n MethodArgumentNotValidException
	 * @return ResponseEntity ValidationErrorResponseDto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleException(MethodArgumentNotValidException ex) {
		log.error("Error de validacion de campos.",ex);
		ValidationErrorResponseDto response = new ValidationErrorResponseDto();
		response.setDate(new Date());
		response.setMessage(this.validationError);
		response.setCause(ex.getLocalizedMessage());
		
		List<FieldErrorDto> errors = getFieldErrorDtoList(
				ex.getBindingResult().getFieldErrors());
		response.setFieldErrors(errors);
		return new ResponseEntity<ValidationErrorResponseDto>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handler MethodArgumentTypeMismatchException utilizado para validaciones de campos de entrada referentes a tipo de dato de un servicio RES.
	 * @param ex Excepci&oacute;n MethodArgumentNotValidException
	 * @return ResponseEntity Devuelve el objeto de error de validaci&oacute;n con c&oacute;digo de error 400
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleException(MethodArgumentTypeMismatchException ex) {
		log.error("Error de validacion de campos.",ex);
		ValidationErrorResponseDto response = new ValidationErrorResponseDto();
		response.setDate(new Date());
		response.setMessage(this.validationError);
		response.setCause(ex.getLocalizedMessage());
		return new ResponseEntity<ValidationErrorResponseDto>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Handler ConstraintViolationException utilizado para validaciones de campos de entrada validando las restricciones.
	 * @param ex Excepci&oacute;n ConstraintViolationException
	 * @return ResponseEntity Devuelve el objeto de error de validaci&oacute;n con c&oacute;digo de error 400
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	 public ResponseEntity<ErrorResponseDto> handleException(ConstraintViolationException ex) {
		log.error("Error de validacion de campos por constraint.",ex);
		ErrorResponseDto response = new ErrorResponseDto();
		response.setDate(new Date());
		response.setMessage(this.constraintError);
		response.setCause(ex.getLocalizedMessage());
		return new ResponseEntity<ErrorResponseDto>(response, HttpStatus.BAD_REQUEST);
	 }
	
	/**
	 * Handler error 500
	 * @param ex Excepci&oacute;n producida por el sistema.
	 * @return ResponseEntity Devuelve el objeto de error con c&oacute;digo de error 500
	 */
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        log.error("Error no identificado.",ex);
        ErrorResponseDto response = new ErrorResponseDto();
        response.setDate(new Date());
		response.setMessage(this.internalError);
		response.setCause(ex.getLocalizedMessage());
        return new ResponseEntity<ErrorResponseDto>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	/**
	 * Ontiene una lista de FieldErrorDto de una de FieldError
	 * @param fieldErrorList Lista de errores
	 * @return List<FieldErrorDto>
	 */
	private List<FieldErrorDto> getFieldErrorDtoList(List<FieldError> fieldErrorList){
		List<FieldErrorDto> fieldErrorDtoList = new ArrayList<FieldErrorDto>();
		for(FieldError fieldError : fieldErrorList){
			fieldErrorDtoList.add(mapToFieldErrorDto(fieldError));
		}
		return fieldErrorDtoList;
	}
	
	/**
	 * Mapea FieldError to FieldErrorDto
	 * @param fieldError FieldError
	 * @return FieldErrorDto
	 */
	private FieldErrorDto mapToFieldErrorDto(FieldError fieldError){
		return new FieldErrorDto(fieldError.getField(),fieldError.getDefaultMessage());		
	}
}
