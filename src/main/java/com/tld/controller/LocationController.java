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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.tld.dto.LocationDTO;
import com.tld.dto.info.LocationInfoDTO;
import com.tld.service.LocationService;
import com.tld.util.LogUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/location")
@RequiredArgsConstructor
@Tag(name = "Location Controller", description = "Operaciones para gestionar ubicaciones")
public class LocationController {

	private final LocationService locationService;	
	
	@PostMapping
	  @Operation(summary = "Crear una nueva ubicación", description = "Crea una nueva ubicación y la asocia al usuario autenticado.")
    	@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación creada exitosamente",  
        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationInfoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación o integridad"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
	public ResponseEntity <?> addLocation(@RequestBody LocationDTO locationDTO){	
		LogUtil.log(LocationController.class, Level.INFO, "Solicitud recibida en controller addLocation");
	    return ResponseEntity.ok(locationService.addLocation(locationDTO));			
	}
	
	
	//Parametro field puede ser; ciudad, pais, direccion, id, usuario
	//Parametro value puede ser; iquique, chile, calleX, 1, luis
	@GetMapping
	  @Operation(summary = "Buscar ubicaciones", description = "Obtiene una lista de ubicaciones filtrando por campo y valor.")
    	@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ubicaciones obtenida exitosamente",  
        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationInfoDTO.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
	public ResponseEntity<?> getLocations(@RequestParam String field, @RequestParam String value){
		LogUtil.log(LocationController.class, Level.INFO, "Solicitud recibida en controller getLocations");
		return new  ResponseEntity<>(locationService.getLocations(field, value),HttpStatus.OK);				
	}
	
		
	
	@PutMapping("{locationId}")
    @Operation(summary = "Actualizar una ubicación", description = "Actualiza los datos de una ubicación existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación actualizada exitosamente",  
        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationInfoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
	public ResponseEntity <?> updateLocation(@PathVariable Long locationId, @RequestBody LocationDTO locationDTO){	
		LogUtil.log(LocationController.class, Level.INFO, "Solicitud recibida en controller updateLocation");
	    return ResponseEntity.ok(locationService.updateLocation(locationId, locationDTO));				
	}
	
	
	@DeleteMapping("{locationId}")
    @Operation(summary = "Eliminar (inactivar) una ubicación", description = "Inactiva una ubicación existente si aún está activa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación inactivada exitosamente",  
        		content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationInfoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada"),
        @ApiResponse(responseCode = "400", description = "Ubicación ya se encuentra inactiva"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
	public ResponseEntity <?> deleteLocation(@PathVariable Long locationId){	
		LogUtil.log(LocationController.class, Level.INFO, "Solicitud recibida en controller deleteLocation");
		return ResponseEntity.ok(locationService.deleteLocation(locationId));
	}
}
