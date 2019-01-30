package com.indigo.indvalid.rest.controller;

import static com.indigo.indvalid.rest.util.LogVariable.ID;
import javax.validation.Valid;
import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.indigo.indvalid.jms.model.Message;
import com.indigo.indvalid.jms.service.MessagerProcessorService;
import com.indigo.indvalid.rest.exception.ErrorResponseDto;
import com.indigo.indvalid.rest.exception.ValidationErrorResponseDto;

@Api(tags = "Queue de mensajes Indvalid")
@Validated
@RestController
@RequestMapping(value = "/indvalid")
public class MessengerController {
	
	@Autowired
	private MessagerProcessorService messagerProcessorService;

	@ApiOperation(value = "Envia un mensaje a la Queue de Indvalid.", notes = "Se asegura de completar el proceso ejecutado mediante el mensaje proporcionado.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Mensaje entregado correctamente. Proceso derivado, ejecutado correctamente", response = Boolean.class),
			@ApiResponse(code = 202, message = "Cuando ocurre alguna excepci\u00f3n y/o no cumple con ciertas caracter\u00EDsticas para enviar el mensaje.", response = ErrorResponseDto.class),
			@ApiResponse(code = 400, message = "Datos de la petici\u00f3n incorrectos.", response = ValidationErrorResponseDto.class),
			@ApiResponse(code = 500, message = "Cuando ocurre un error no identificado en el sistema.", response = ErrorResponseDto.class) })
	@PostMapping(path = "/sendMessage", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Message> sendMessage(
			@Valid @ApiParam(value = "Mensaje enviado por indvalid para realizar un proceso", required = true) @RequestBody Message message) {
		MDC.put(ID, message.getId().toString());
		messagerProcessorService.process(message);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
}
