package com.tld.controller;

import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import com.tld.dto.SensorDTO;
import com.tld.dto.info.SensorInfoDTO;
import com.tld.service.SensorService;
import com.tld.util.LogUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/sensor")
@RequiredArgsConstructor
@Tag(name = "Sensores Controller", description = "Operaciones relacionadas con sensores")
public class SensorController {
	
	private final SensorService sensorService;
	
	
	@PostMapping
	@Operation(
		    summary = "Crear un nuevo sensor",
		    description = "Permite registrar un nuevo sensor en el sistema. Es necesario validar que la ubicación y categoría existan, y que estén asociadas a la compañía correspondiente."
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Sensor creado exitosamente", content = @Content(schema = @Schema(implementation = SensorInfoDTO.class))),
		    @ApiResponse(responseCode = "404", description = "Ubicación o categoría no encontrada"),
		    @ApiResponse(responseCode = "400", description = "Datos inválidos o sensor ya existente")
		})
	public ResponseEntity <?> addSensor( @Parameter(description = "DTO del sensor a crear", required = true) @RequestBody SensorDTO sensorDTO, 
			 @Parameter(description = "API Key de la compañía", required = true) @RequestHeader("company_api_key") String companyApiKey){
		LogUtil.log(SensorController.class, Level.INFO, "Solicitud recibida en controller addSensor");		
		return ResponseEntity.ok(sensorService.addSensor(sensorDTO,companyApiKey));
	}	
	
	@PutMapping("{sensorId}")
	@Operation(
		    summary = "Actualizar sensor",
		    description = "Actualiza los datos de un sensor existente. Solo se permite si el sensor está asociado a la compañía identificada por el API Key."
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Sensor actualizado exitosamente", content = @Content(schema = @Schema(implementation = SensorInfoDTO.class))),
		    @ApiResponse(responseCode = "404", description = "Sensor, ubicación o categoría no encontrada"),
		    @ApiResponse(responseCode = "400", description = "Datos inválidos o clave API ya usada por otro sensor")
		})
	public ResponseEntity <?> updateSensor( @Parameter(description = "ID del sensor a actualizar", required = true) @PathVariable Long sensorId, 
			@Parameter(description = "DTO con los nuevos datos del sensor", required = true) @RequestBody SensorDTO sensorDTO, 
			@Parameter(description = "API Key de la compañía", required = true) @RequestHeader("company_api_key") String companyApiKey){	
		LogUtil.log(SensorController.class, Level.INFO, "Solicitud recibida en controller updateSensor");
		sensorDTO.setSensorId(sensorId);	    		    			
	    return ResponseEntity.ok(sensorService.updateSensor(companyApiKey,sensorDTO));		
	}	
	
	
	@GetMapping
	@Operation(
		    summary = "Buscar sensores",
		    description = "Permite buscar sensores asociados a una compañía según un campo (nombre, ubicación, ID, etc.) y su valor."
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Lista de sensores encontrados", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SensorInfoDTO.class)))),
		    @ApiResponse(responseCode = "404", description = "No se encontraron sensores con los criterios entregados")
		})
   	public ResponseEntity<?> getSensors(  @Parameter(description = "Campo por el que se desea filtrar (sensorName, id, locationId, etc.)", required = true) @RequestParam String field, 
   		 @Parameter(description = "Valor del campo por el que se filtra", required = true)	@RequestParam String value,
   		 @Parameter(description = "API Key de la compañía", required = true)	@RequestHeader("company_api_key") String companyApiKey){  		
		LogUtil.log(SensorController.class, Level.INFO, "Solicitud recibida en controller getSensors");
		return new  ResponseEntity<>(sensorService.getSensors(field, value, companyApiKey),HttpStatus.OK);			
   	}
       
    
    
    @DeleteMapping("{sensorId}")
    @Operation(
    	    summary = "Eliminar (inhabilitar) sensor",
    	    description = "Marca un sensor como inactivo. Solo se puede realizar si el sensor pertenece a la compañía identificada por el API Key."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Sensor inhabilitado exitosamente", content = @Content(schema = @Schema(implementation = SensorInfoDTO.class))),
    	    @ApiResponse(responseCode = "404", description = "Sensor no encontrado o no pertenece a la compañía")
    	})
	public ResponseEntity <?>  deleteSensor( @Parameter(description = "ID del sensor a eliminar", required = true) @PathVariable Long sensorId,
			 @Parameter(description = "API Key de la compañía", required = true)	@RequestHeader("company_api_key") String companyApiKey){		
    	LogUtil.log(SensorController.class, Level.INFO, "Solicitud recibida en controller deleteSensor");
    	return new  ResponseEntity<>(sensorService.deleteSensor(sensorId, companyApiKey),HttpStatus.OK);	    	
	}
	
	
}
