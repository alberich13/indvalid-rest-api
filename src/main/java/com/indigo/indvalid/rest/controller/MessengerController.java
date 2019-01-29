package com.indigo.indvalid.rest.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "Bloqueo de expedientes")
@Validated
@RestController
@RequestMapping(value = "/indvalid-messenger")
public class MessengerController {
	
	@ApiOperation(value = "Envía un mensaje a la Queue de Indvalid.", 
			notes = "Se asegura de completar el proceso ejecutado mediante el mensaje proporcionado.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cambio aplicado correctamente, el expediente ha sido bloqueado para consulta", response = Boolean.class),
			@ApiResponse(code = 202, message = "Cuando ocurre alguna excepci\u00f3n y/o no cumple con ciertas caracter\u00EDsticas para enviar el mensaje.", response = ErrorResponseDto.class),
			@ApiResponse(code = 400, message = "Datos de la petici\u00f3n incorrectos.", response = ValidationErrorResponseDto.class),			
			@ApiResponse(code = 500, message = "Cuando ocurre un error no identificado en el sistema.", response = ErrorResponseDto.class) })
	@PutMapping(path = "/files/{fid}/lock", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> sendMessage(@Min(1) @Max(Integer.MAX_VALUE)
								@ApiParam(value = "Identificador del expediente de una persona.", required = true)
								@PathVariable Integer fid){
		MDC.put(FID, fid.toString());
		ContractLockDto contractLockDto = contractService.findContract(fid, false);
		return new ResponseEntity<>(lockService.lockFile(contractLockDto, true), HttpStatus.OK);
	}
}
