package com.tld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tld.dto.MeasurementDTO;
import com.tld.dto.info.MeasurementInfoDTO;
import com.tld.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MeasurementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MeasurementService measurementService;

    @InjectMocks
    private MeasurementController measurementController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(measurementController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddSensorData_Success() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setMeasurementId(1L);
        measurementDTO.setApi_key("sensor-api-key");
        
        doNothing().when(measurementService).addSensorData(any(MeasurementDTO.class), eq("sensor-api-key"));

        mockMvc.perform(post("/api/v1/measurement")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("sensor_api_key", "sensor-api-key")
                            .content(objectMapper.writeValueAsString(measurementDTO)))
                    .andExpect(status().isCreated());
    }


    @Test
    void testUpdateSensorData_Success() throws Exception {
        Long measurementId = 1L;
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setMeasurementId(measurementId);
        measurementDTO.setApi_key("sensor-api-key");

        MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();
        
        when(measurementService.updateSensorData(eq("sensor-api-key"), eq(measurementId), eq("company-api-key"))).thenReturn(measurementInfoDTO);

        mockMvc.perform(put("/api/v1/measurement")
                        .param("id", measurementId.toString())
                        .param("sensorApiKey", "sensor-api-key")
                        .header("company_api_key", "company-api-key"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testGetSensorById_Success() throws Exception {
        Long measurementId = 1L;
        MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();

        when(measurementService.getSensorDataById(eq(measurementId), eq("sensor-api-key"), eq("company-api-key"))).thenReturn(measurementInfoDTO);

        mockMvc.perform(get("/api/v1/measurement")
                        .param("measurementID", measurementId.toString())
                        .param("sensorApiKey", "sensor-api-key")
                        .header("company_api_key", "company-api-key"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testGetSensorByEpoch_Success() throws Exception {
        Long from = 1712672000L;
        Long to = 1712758400L;
        MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();

        when(measurementService.getSensorDataByEpoch(eq(from), eq(to), eq("company-api-key"))).thenReturn(Collections.singletonList(measurementInfoDTO));

        mockMvc.perform(get("/api/v1/measurement/epoch")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .header("company_api_key", "company-api-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testGetSensorByCompany_Success() throws Exception {
        MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();

        when(measurementService.getSensorDataByCompany(eq("company-api-key"))).thenReturn(Collections.singletonList(measurementInfoDTO));

        mockMvc.perform(get("/api/v1/measurement/company")
                        .header("company_api_key", "company-api-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testDeleteSensorData_Success() throws Exception {
        Long measurementId = 1L;

        MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();
        measurementInfoDTO.setId(measurementId);
        measurementInfoDTO.setSensor_api_key("sensor-api-key");
        measurementInfoDTO.setSensor_name("Sensor1");
        measurementInfoDTO.setCompany_name("CompanyName");
        measurementInfoDTO.setLocation_adress("Address");
        measurementInfoDTO.setCity_name("City");
        measurementInfoDTO.setRegion_name("Region");
        measurementInfoDTO.setCountry_name("Country");
        measurementInfoDTO.setRecord_saved_at("2025-04-15T12:00:00");
        measurementInfoDTO.setRecord_modified_at("2025-04-15T12:30:00");
        measurementInfoDTO.setIs_record_active(true);
        measurementInfoDTO.setRecords(new ArrayList<>()); // Empty list or mock data as needed

        when(measurementService.deleteSensorData(eq("sensor-api-key"), eq(measurementId), eq("company-api-key")))
            .thenReturn(measurementInfoDTO);

        mockMvc.perform(delete("/api/v1/measurement")
                            .param("id", measurementId.toString())
                            .param("sensorApiKey", "sensor-api-key")
                            .header("company_api_key", "company-api-key"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"id\":1,\"sensor_api_key\":\"sensor-api-key\",\"sensor_name\":\"Sensor1\",\"company_name\":\"CompanyName\",\"location_adress\":\"Address\",\"city_name\":\"City\",\"region_name\":\"Region\",\"country_name\":\"Country\",\"record_saved_at\":\"2025-04-15T12:00:00\",\"record_modified_at\":\"2025-04-15T12:30:00\",\"is_record_active\":true,\"records\":[]}"));
    }



}
