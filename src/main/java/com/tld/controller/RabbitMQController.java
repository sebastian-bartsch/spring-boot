package com.tld.controller;

import java.util.Random;
import java.util.logging.Level;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tld.service.rabbit.RabbitMQProducer;
import com.tld.util.LogUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/rabbit")
@Tag(name = "Rabbit Controller", description = "Operaciones relacionadas con Rabbit")
public class RabbitMQController {

    private final RabbitMQProducer producer;   

    public RabbitMQController(RabbitMQProducer producer) {
        this.producer = producer;
    }
       
    @PostMapping("/add")
    @Operation(
    	    summary = "Enviar mensaje individual desde un sensor",
    	    description = "Permite enviar un mensaje único desde un sensor a través de RabbitMQ. El mensaje puede incluir datos del sensor y debe ir acompañado de una API Key en el header para validación."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "201", description = "Mensaje enviado con éxito"),
    	    @ApiResponse(responseCode = "503", description = "No se pudo conectar a RabbitMQ. Intenta más tarde."),
    	    @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    	})
    public ResponseEntity<String> sendSensorData(@RequestBody String message, @RequestHeader(value ="sensor_api_key", required = false) String sensorApiKey) {
        try {
        	
        	LogUtil.log(RabbitMQController.class, Level.INFO, "Solicitud recibida en controller sendSensorData / Rabbit");
            String messageWithApiKey = sensorApiKey + "|" + message; // Concatenar API Key con el mensaje      
            
            boolean messageSent = producer.sendMessage(messageWithApiKey);
            
            if (messageSent) {
                return new ResponseEntity<>("Mensaje enviado con éxito", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("No se pudo conectar a RabbitMQ. Intenta más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return new ResponseEntity<>("Error inesperado. Intenta más tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
    @PostMapping("/masivo/{nTimes}")
    @Operation(
    	    summary = "Enviar mensajes masivos desde un sensor",
    	    description = "Permite enviar un mismo mensaje múltiples veces hacia RabbitMQ. Entre cada envío se genera una espera aleatoria de entre 1 y 30 segundos. Requiere una API Key de sensor en el header."
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Mensajes enviados correctamente"),
    	    @ApiResponse(responseCode = "400", description = "Solicitud inválida (por ejemplo, API Key faltante)")
    	})
    public String sendMessageNTtimes(@PathVariable Integer nTimes, @RequestBody String message, @RequestHeader("sensor_api_key") String sensorApiKey) {
    	String messageWithApiKey = sensorApiKey + "|" + message; // Concatenar API Key con el mensaje
    	Random random = new Random();
    	
    	for (int i=0; i < nTimes; i++ ) {
    		producer.sendMessage(messageWithApiKey);  		
			try {
				Thread.sleep(random.nextInt(1,31)* 1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
    	}
       return " Se han enviado los "+nTimes+" mensajes a RabbitMQ con API Key";
    	
    }
    
    
}