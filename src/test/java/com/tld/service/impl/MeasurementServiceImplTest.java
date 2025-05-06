package com.tld.service.impl;

import com.tld.dto.MeasurementDTO;
import com.tld.entity.Measurement;
import com.tld.entity.Sensor;
import com.tld.entity.Company;
import com.tld.jpa.repository.SensorRepository;
import com.tld.jpa.repository.MeasurementRepository;
import com.tld.jpa.repository.CompanyRepository;
import com.tld.mapper.MeasurementMapper;
import com.tld.util.LogUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MeasurementServiceImplTest {

	 @InjectMocks
	    private MeasurementServiceImpl measurementService;

	    @Mock
	    private SensorRepository sensorRepository;

	    @Mock
	    private MeasurementRepository measurementRepository;

	    @Mock
	    private CompanyRepository companyRepository; 

	    @Mock
	    private MeasurementMapper measurementMapper;

	    @Mock
	    private LogUtil logUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSensorData_InvalidApiKey() {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setApi_key("invalid-api-key");

        when(sensorRepository.findBySensorApiKey("invalid-api-key")).thenReturn(java.util.Optional.empty());

        try {
            measurementService.addSensorData(measurementDTO, "invalid-api-key");
        } catch (Exception e) {
            assertTrue(e instanceof com.tld.exception.EntityNotFoundException);
            assertEquals("Sensor / Api key no encontrado. No se grabarán metricas recibidas", e.getMessage());
        }

        verify(sensorRepository, times(1)).findBySensorApiKey("invalid-api-key");
    }
    
    @Test
    void testUpdateSensorData_InvalidCompanySensorRelation() {
        String companyApiKey = "valid-company-api-key";
        String sensorApiKey = "valid-sensor-api-key";
        Long measurementId = 1L;

        Company company = new Company();
        company.setCompanyApiKey(companyApiKey);

        Measurement measurement = new Measurement();
        measurement.setMeasurementId(measurementId);
        measurement.setSensor(new Sensor());

        when(companyRepository.findByCompanyApiKey(companyApiKey))
                .thenReturn(java.util.Optional.of(company));  

        when(measurementRepository.findById(measurementId))
                .thenReturn(java.util.Optional.of(measurement));  

        when(sensorRepository.findIfCompanyAndSensorAreOk(anyLong(), anyLong()))
                .thenReturn((short) 0);  

        try {
            measurementService.updateSensorData(sensorApiKey, measurementId, companyApiKey);
        } catch (Exception e) {
            assertTrue(e instanceof com.tld.exception.InvalidCompanySensorException);
            assertEquals("Solo puedes modificar cuando tu compania y el sensor estan relacionados", e.getMessage());
        }

        verify(measurementRepository, times(0)).save(any());  
    }
   
}
