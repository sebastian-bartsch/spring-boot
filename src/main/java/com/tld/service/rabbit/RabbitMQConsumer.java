package com.tld.service.rabbit;

import java.util.logging.Level;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.tld.dto.MeasurementDTO;
import com.tld.mapper.MeasurementMapper;
import com.tld.service.impl.MeasurementServiceImpl;
import com.tld.util.LogUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {
	//private final RestTemplate restTemplate;	
	private final MeasurementServiceImpl measurementServiceImpl;
	private final MeasurementMapper measurementMapper;
	
//	private final RestTemplate restTemplate = new RestTemplate();
	
/*	@RabbitListener(queues = "sensor_cola")
    public void receiveSensorData(String message) {
        System.out.println("✅ Recibido en TEST: " + message);
    }*/
	/*
	 private final String URL_DESTINO = "http://localhost:8080/api/v1/measurement";  // Cambia esto por tu endpoint real

	    @RabbitListener(queues = "sensor_cola")
	    public void receiveMessage(String message) {
	        System.out.println("📥 Mensaje recibido: " + message);
	        
	       
	        String[] split = message.split("\\|", 2);
	        if (split.length < 2) {
	            System.err.println("❌ Mensaje mal formado, no tiene API Key");
	            return;
	        }
	        
	        String sensorApiKey = split[0];
	        String json = split[1];

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("sensor_api_key", sensorApiKey);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
	        
	        //Enviar la solicitud POST al endpoint
	        try {
	        ResponseEntity<String> response = restTemplate.exchange(URL_DESTINO, HttpMethod.POST, requestEntity, String.class);       
	        //String respuesta = restTemplate.postForObject(URL_DESTINO, json, String.class);
	        System.out.println("📤 Respuesta del endpoint: " + response);
		    } catch (Exception e) {
		       System.out.println("❌ Error al enviar datos al endpoint: {}"+ e.getMessage());
		    }
	    }
	    
	    */

    @RabbitListener(queues = "sensor_cola")
    public void receiveSensorData(String message) {
    	LogUtil.log(RabbitMQConsumer.class, Level.INFO, "Solicitud recibida en consumer");             
    	try {
	        String[] split = message.split("\\|", 2);	        
	        if (split.length < 1) {
	        	LogUtil.log(RabbitMQConsumer.class, Level.WARNING, "Solicitud no tiene json y header"); 
	            throw new com.tld.exception.MissingParameterException("Faltan parametros en peticion");
	        }      	        
	        
	        String sensorApiKey = split.length > 1 ? split[0] : null;
	        String json = split.length > 1 ? split[1] : split[0];	            
	        
	        MeasurementDTO measurementDTO = measurementMapper.fromJsonToDTO(json);
	        
	        if(measurementDTO.getApi_key()==null) {
	        	measurementDTO.setApi_key(sensorApiKey);        	
	        }     	        
	        
        	measurementServiceImpl.addSensorDataRabbit(measurementDTO);    

	        LogUtil.log(RabbitMQConsumer.class, Level.WARNING, "Enviada la solicitud a measurementServiceImpl > addSensorDataRabbit"); 
		 } catch (com.tld.exception.EntityNotFoundException e) {
            LogUtil.log(RabbitMQConsumer.class, Level.WARNING, "Entidad no encontrada, desde MeasurementServiceImpl: " + e.getMessage());
		 } catch (com.tld.exception.MissingParameterException e) {
            LogUtil.log(RabbitMQConsumer.class, Level.WARNING, "Faltan parámetros en la petición: " + e.getMessage());
		 } catch (Exception e) {
            LogUtil.log(RabbitMQConsumer.class, Level.SEVERE, "Error al procesar el mensaje: " + e.getMessage());
		 }
                
    }
    
}