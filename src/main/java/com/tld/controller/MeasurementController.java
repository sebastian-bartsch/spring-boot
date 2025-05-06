package com.tld.controller;

import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tld.dto.MeasurementDTO;
import com.tld.dto.info.MeasurementInfoDTO;
import com.tld.service.MeasurementService;
import com.tld.util.LogUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/measurement")
@RequiredArgsConstructor
@Tag(name = "measurement Controller", description = "Operaciones relacionadas con la Data del Sensor")

public class MeasurementController {
	
	private final MeasurementService measurementService;								
	
	@PostMapping
	  @Operation(
		        summary = "Crear una nueva medición",
		        description = "Registra una nueva medición asociada a un sensor."
		    )
		    @ApiResponses(value = {
		        @ApiResponse(responseCode = "201", description = "Medición creada con éxito"),
		        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
		        @ApiResponse(responseCode = "401", description = "No autorizado"),
		        @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
		    })
	public ResponseEntity <String> addSensorData(@RequestBody MeasurementDTO measurementDTO, @RequestHeader(value ="sensor_api_key", required = false) String sensorApiKey){
		LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller addSensorData");	
		//measurementDTO.setApi_key(sensorApiKey);
		measurementService.addSensorData(measurementDTO, sensorApiKey);
		return ResponseEntity.status(HttpStatus.CREATED).body("Métrica registrada con éxito");

	}	
	
	@PutMapping
	   @Operation(
		        summary = "Actualizar una medición",
		        description = "Actualiza los datos de una medición específica. Requiere API Key del sensor y de la compañía."
		    )
		    @ApiResponses(value = {
		        @ApiResponse(responseCode = "200", description = "Métrica actualizada con éxito" ,  
		        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementInfoDTO.class))),
		        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
		        @ApiResponse(responseCode = "401", description = "API Key inválida"),
		        @ApiResponse(responseCode = "404", description = "Métrica no encontrada"),
		        @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
		    })
	public ResponseEntity <?> updateSensorData( @Parameter(description = "ID de la medición a actualizar", example = "101", required = true) @RequestParam Long id, 
	        @Parameter(description = "API Key del sensor", example = "SENSOR-123456789", required = true) @RequestParam String sensorApiKey, 
	        @Parameter(description = "API Key de la compañía", example = "COMPANY-987654321", required = true) @RequestHeader("company_api_key") String companyApiKey){		
		LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller updateSensorData");
		return ResponseEntity.ok(measurementService.updateSensorData(sensorApiKey, id, companyApiKey));
		
	}	
	
	
	@GetMapping
    @Operation(
            summary = "Obtener medición por ID",
            description = "Obtiene los datos de una medición específica usando su ID, la API Key del sensor y la de la compañía."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métrica obtenida con éxito",  
	        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementInfoDTO.class))),
            @ApiResponse(responseCode = "401", description = "API Key inválida"),
            @ApiResponse(responseCode = "404", description = "Métrica no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
        })
   	public ResponseEntity<?> getSensorById( @Parameter(description = "ID de la medición", example = "101", required = true) @RequestParam Long measurementID, 
   		 @Parameter(description = "API Key del sensor", example = "SENSOR-123456789", required = true) @RequestParam String sensorApiKey, 
         @Parameter(description = "API Key de la compañía", example = "COMPANY-987654321", required = true) @RequestHeader("company_api_key") String companyApiKey){    
		LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller getSensorById");
		return new  ResponseEntity<>(measurementService.getSensorDataById(measurementID,sensorApiKey,companyApiKey),HttpStatus.OK);		
   	}
	
	@GetMapping("epoch")
	@Operation(
	        summary = "Obtener mediciones por rango de tiempo",
	        description = "Obtiene todas las mediciones dentro de un rango de tiempo especificado (formato epoch). Requiere API Key de compañía."
	    )
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Datos obtenidos con éxito",  
	        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementInfoDTO.class))),
	        @ApiResponse(responseCode = "400", description = "Parámetros de tiempo inválidos"),
	        @ApiResponse(responseCode = "401", description = "API Key de compañía inválida"),
	        @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
	    })
   	public ResponseEntity<?> getSensorByEpoch( @Parameter(description = "Tiempo inicial en formato epoch", example = "1712672000", required = true) @RequestParam Long from, 
   			@Parameter(description = "Tiempo final en formato epoch", example = "1712758400", required = true) @RequestParam Long to, 
   		  @Parameter(description = "API Key de la compañía", example = "COMPANY-987654321", required = true) @RequestHeader("company_api_key") String companyApiKey){    
		LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller getSensorByEpoch");
		return new  ResponseEntity<>(measurementService.getSensorDataByEpoch(from, to, companyApiKey),HttpStatus.OK);			
   	}
	
	@GetMapping("company")
	  @Operation(
		        summary = "Obtener todas las mediciones de una compañía",
		        description = "Devuelve todas las mediciones asociadas a una compañía autenticada mediante su API Key."
		    )
		    @ApiResponses(value = {
		        @ApiResponse(responseCode = "200", description = "Datos obtenidos con éxito",  
		        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementInfoDTO.class))),
		        @ApiResponse(responseCode = "401", description = "API Key de compañía inválida"),
		        @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
		    })
   	public ResponseEntity<?> getSensorByCompany( @Parameter(description = "API Key de la compañía", example = "COMPANY-987654321", required = true) @RequestHeader("company_api_key") String companyApiKey){ 
		LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller getSensorByCompany");
		return new  ResponseEntity<>(measurementService.getSensorDataByCompany(companyApiKey),HttpStatus.OK);			
   	}      
    
    
    @DeleteMapping
    @Operation(
            summary = "Eliminar una medición",
            description = "Elimina una medición específica a partir de su ID. Requiere API Key del sensor y de la compañía."
        )
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métrica eliminada con éxito",  
	        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeasurementInfoDTO.class))),
            @ApiResponse(responseCode = "401", description = "API Key inválida"),
            @ApiResponse(responseCode = "404", description = "Métrica no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
        })
	public ResponseEntity <?> deleteSensorData( @Parameter(description = "ID de la medición", example = "101", required = true) @RequestParam Long id, 
			 @Parameter(description = "API Key del sensor", example = "SENSOR-123456789", required = true) @RequestParam String sensorApiKey, 
			 @Parameter(description = "API Key de la compañía", example = "COMPANY-987654321", required = true) @RequestHeader("company_api_key") String companyApiKey){
    	LogUtil.log(MeasurementController.class, Level.INFO, "Solicitud recibida en controller deleteSensorData");
    	return new  ResponseEntity<>(measurementService.deleteSensorData(sensorApiKey,id,companyApiKey),HttpStatus.OK);
	}
}
