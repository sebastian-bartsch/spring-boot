package com.tld.controller;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.tld.configuration.jwt.JwtUtils;
import com.tld.service.rabbit.RabbitMQProducer;

@SpringBootTest
class RabbitMQControllerTest {

	 private MockMvc mockMvc;

	    @MockBean  
	    private RabbitMQProducer producer;
	    
	    @MockBean  
	    private JwtUtils jwtUtils; 

	    @Autowired  
	    private RabbitMQController rabbitMQController;

	    @BeforeEach
	    void setUp() {
	        mockMvc = MockMvcBuilders.standaloneSetup(rabbitMQController)
	                .build();
	    }

	    @Test
	    void testSendSensorData_Success() throws Exception {
	        String message = "Test message";
	        String sensorApiKey = "sensor-api-key";
	        
	        when(producer.sendMessage(anyString())).thenReturn(true);

	        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rabbit/add")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(message)
	                        .header("sensor_api_key", sensorApiKey))
	                .andExpect(MockMvcResultMatchers.status().isCreated())
	                .andExpect(MockMvcResultMatchers.content().string("Mensaje enviado con éxito"));
	    }

	    @Test
	    void testSendSensorData_Failure() throws Exception {
	        String message = "Test message";
	        String sensorApiKey = "sensor-api-key";
	        
	        when(producer.sendMessage(anyString())).thenReturn(false);

	        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/rabbit/add")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(message)
	                        .header("sensor_api_key", sensorApiKey))
	                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
	                .andExpect(MockMvcResultMatchers.content().string("No se pudo conectar a RabbitMQ. Intenta más tarde."));
	    }
}
