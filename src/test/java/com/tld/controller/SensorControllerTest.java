package com.tld.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tld.dto.SensorDTO;
import com.tld.dto.info.SensorInfoDTO;
import com.tld.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SensorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private SensorController sensorController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sensorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddSensor_Success() throws Exception {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setSensorName("Sensor A");

        SensorInfoDTO sensorInfo = new SensorInfoDTO();
        sensorInfo.setSensorName("Sensor A");

        when(sensorService.addSensor(any(), eq("api-key-123"))).thenReturn(sensorInfo);

        mockMvc.perform(post("/api/v1/sensor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("company_api_key", "api-key-123")
                        .content(objectMapper.writeValueAsString(sensorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorName").value("Sensor A"));
    }

    @Test
    void testUpdateSensor_Success() throws Exception {
        Long sensorId = 1L;
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setSensorName("Sensor Updated");

        SensorInfoDTO sensorInfo = new SensorInfoDTO();
        sensorInfo.setSensorName("Sensor Updated");

        when(sensorService.updateSensor(eq("api-key-123"), any(SensorDTO.class))).thenReturn(sensorInfo);

        mockMvc.perform(put("/api/v1/sensor/{sensorId}", sensorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("company_api_key", "api-key-123")
                        .content(objectMapper.writeValueAsString(sensorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorName").value("Sensor Updated"));
    }

    @Test
    void testGetSensors_Success() throws Exception {
        SensorInfoDTO sensorInfo = new SensorInfoDTO();
        sensorInfo.setSensorName("Sensor A");

        when(sensorService.getSensors("sensorName", "Sensor A", "api-key-123"))
                .thenReturn(List.of(sensorInfo));

        mockMvc.perform(get("/api/v1/sensor")
                        .param("field", "sensorName")
                        .param("value", "Sensor A")
                        .header("company_api_key", "api-key-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sensorName").value("Sensor A"));
    }

    @Test
    void testDeleteSensor_Success() throws Exception {
        Long sensorId = 1L;
        SensorInfoDTO sensorInfo = new SensorInfoDTO();
        sensorInfo.setSensorName("Sensor Deleted");

        when(sensorService.deleteSensor(sensorId, "api-key-123")).thenReturn(sensorInfo);

        mockMvc.perform(delete("/api/v1/sensor/{sensorId}", sensorId)
                        .header("company_api_key", "api-key-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensorName").value("Sensor Deleted"));
    }
}

