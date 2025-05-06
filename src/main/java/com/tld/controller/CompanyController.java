package com.tld.controller;




import java.util.logging.Level;

import com.tld.util.LogUtil;
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
import com.tld.dto.CompanyDTO;
import com.tld.dto.info.CompanyInfoDTO;
import com.tld.service.CompanyService;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;



@RestController
@RequestMapping("api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Company Controller", description = "Gestion de Compañias")
public class CompanyController {
	
    private final CompanyService companyService;
    

    @PostMapping
    @Operation(
    	    summary = "Agregar una nueva compañía",
    	    description = "Permite agregar una nueva compañía al sistema. Solo los usuarios autenticados pueden realizar esta operación."
    	)
    @ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Compañía agregada exitosamente", 
    	        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyInfoDTO.class))),
    	    @ApiResponse(responseCode = "400", description = "Solicitud inválida o campos faltantes"),
    	    @ApiResponse(responseCode = "409", description = "Violación de restricción única (nombre de compañía duplicado)"),
    	    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    	})
    public ResponseEntity<?> addCompany(@RequestBody CompanyDTO companyDTO) {	
        LogUtil.log(CompanyController.class, Level.INFO, "Solicitud recibida en controller addCompany");
        return ResponseEntity.ok(companyService.addCompany(companyDTO));
    }

    
   
    @PutMapping("{companyId}")
    @Operation(
    	    summary = "Actualizar una compañía",
    	    description = "Permite actualizar los datos de una compañía existente por su ID. Solo usuarios autenticados pueden realizar esta operación."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Compañía actualizada exitosamente", 
    	        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyInfoDTO.class))),
    	    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
    	    @ApiResponse(responseCode = "404", description = "Compañía no encontrada con el ID proporcionado"),
    	    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    	})
	public ResponseEntity <?> updateCompany(@PathVariable Long companyId, @RequestBody CompanyDTO companyDTO){
    	LogUtil.log(CompanyController.class, Level.INFO, "Solicitud recibida en controller updateCompany");		    		    			
		//CompanyInfoDTO updatedLocation = companyService.updateCompany(companyId, companyDTO);
	    return ResponseEntity.ok(companyService.updateCompany(companyId, companyDTO));	
	}
    
    
    @GetMapping
    @Operation(
    	    summary = "Buscar compañías por campo",
    	    description = "Devuelve una lista de compañías que coinciden con un campo y valor específicos (por ejemplo, nombre)."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Lista de compañías encontrada", 
    	        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CompanyInfoDTO.class)))),
    	    @ApiResponse(responseCode = "404", description = "No se encontraron compañías con los criterios proporcionados"),
    	    @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos"),
    	    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    	})
   	public ResponseEntity<?> getCompanies(@RequestParam String field, @RequestParam String value){   
    	LogUtil.log(CompanyController.class, Level.INFO, "Solicitud recibida en controller getCompanies");		
		return new  ResponseEntity<>(companyService.getCompanies(field, value),HttpStatus.OK);				
   	}
       
    
    
    @DeleteMapping("{companyId}")
    @Operation(
    	    summary = "Eliminar (desactivar) una compañía",
    	    description = "Desactiva una compañía existente por su ID. El registro no se elimina físicamente, solo se marca como inactivo."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Compañía desactivada exitosamente", 
    	        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyInfoDTO.class))),
    	    @ApiResponse(responseCode = "404", description = "Compañía no encontrada con el ID proporcionado"),
    	    @ApiResponse(responseCode = "409", description = "La compañía ya se encuentra inactiva"),
    	    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    	})
	public ResponseEntity <?> deleteCompany(@PathVariable Long companyId){	
    	LogUtil.log(CompanyController.class, Level.INFO, "Solicitud recibida en controller deleteCompany");
    	return ResponseEntity.ok(companyService.deleteCompany(companyId));
	}
	
		
    
}
