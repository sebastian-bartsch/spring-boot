package com.tld.mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tld.dto.MeasurementDTO;
import com.tld.dto.SensorDataDTO;
import com.tld.dto.info.MeasurementInfoDTO;
import com.tld.dto.info.SensorDataInfoDTO;
import com.tld.entity.Measurement;
import com.tld.entity.Sensor;
import com.tld.entity.SensorData;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class MeasurementMapper {
	
	private final SensorDataMapper sensorDataMapper; 
	 private final ObjectMapper objectMapper;
	
	public MeasurementDTO toDTO(Measurement measurement) {
		
		 List<SensorDataDTO> sensorDataDTOList = measurement.getSensorDataList()
	                .stream()
	                .map(sensorDataMapper::toDTO)
	                .collect(Collectors.toList());

	        return new MeasurementDTO(
	                measurement.getMeasurementId(),
	                measurement.getSensor().getSensorApiKey(),
	                measurement.getSensor().getSensorId(),
	                sensorDataDTOList,
	                measurement.getMeasurementIsActive()
	        );     
	}

	public Measurement toEntity(MeasurementDTO measurementDTO) {	 
	       List<SensorData> sensorDataList = measurementDTO.getJson_data()
	                .stream()
	                .flatMap(dto -> sensorDataMapper.toEntityList(dto).stream()) // Convertir múltiples métricas
	                .collect(Collectors.toList());

	        return new Measurement(
	                measurementDTO.getMeasurementId(),
	                new Sensor(measurementDTO.getSensorId()),
	                sensorDataList,
	                measurementDTO.getMeasurementIsActive()
	        );
	}
	
	
	public MeasurementInfoDTO mapToMeasurementInfoDTO(List<Object[]> info) {
	    if (info == null || info.isEmpty()) {
	        throw new IllegalArgumentException("La lista de datos está vacía o es nula.");
	    }

	    Object[] general = info.get(0);
	    MeasurementInfoDTO measurementInfoDTO = new MeasurementInfoDTO();
	    
	    measurementInfoDTO.setId((Long) general[0]);
	    measurementInfoDTO.setSensor_api_key((String) general[1]);
	    measurementInfoDTO.setSensor_name((String) general[2]);
	    measurementInfoDTO.setCompany_name((String) general[3]);
	    measurementInfoDTO.setLocation_adress((String) general[4]);
	    measurementInfoDTO.setCity_name((String) general[5]);
	    measurementInfoDTO.setRegion_name((String) general[6]);
	    measurementInfoDTO.setCountry_name((String) general[7]);
	    measurementInfoDTO.setRecord_saved_at((String) general[8]);
	    measurementInfoDTO.setRecord_modified_at((String) general[9]);
	    measurementInfoDTO.setIs_record_active((Boolean) general[10]);

	    // Mapeo de registros de SensorDataInfoDTO
	    List<SensorDataInfoDTO> sensorDataInfoList = new ArrayList<>();
	    
	    for (Object[] row : info) {
	        SensorDataInfoDTO sensorDataInfoDTO = new SensorDataInfoDTO();
	        sensorDataInfoDTO.setCorrelative((Integer) row[11]);
	        sensorDataInfoDTO.setMetric_id((Integer) row[12]);
	        sensorDataInfoDTO.setMetric_name((String) row[13]);
	        sensorDataInfoDTO.setMetric_value((Double) row[14]);
	        sensorDataInfoDTO.setDatetime_epoch((Long) row[15]);
	        sensorDataInfoDTO.setDatetime_legible(Instant.ofEpochSecond((Long) row[15]));

	        sensorDataInfoList.add(sensorDataInfoDTO);
	    }

	    measurementInfoDTO.setRecords(sensorDataInfoList);
	    
	    return measurementInfoDTO;
	}
	
	
	public List<MeasurementInfoDTO> mapToListMeasurementInfoDTO(List<Object[]> info) {
	//    List<MeasurementInfoDTO> measurementInfoDTOList = new ArrayList<>();
	    
	    // Usamos un Map para almacenar datos generales y evitar duplicados en la medición
	    Map<Long, MeasurementInfoDTO> measurementMap = new HashMap<>();

	    // Iteramos sobre la lista de resultados
	    for (Object[] row : info) {
	        Long measurementId = (Long) row[0]; // ID de la medición
	        MeasurementInfoDTO measurementInfoDTO = measurementMap.get(measurementId);
	        
	        if (measurementInfoDTO == null) {
	            // Si no existe una entrada para esta medición, la creamos
	            measurementInfoDTO = new MeasurementInfoDTO();
	            measurementInfoDTO.setId(measurementId);
	            measurementInfoDTO.setSensor_api_key((String) row[1]);
	            measurementInfoDTO.setSensor_name((String) row[2]);
	            measurementInfoDTO.setCompany_name((String) row[3]);
	            measurementInfoDTO.setLocation_adress((String) row[4]);
	            measurementInfoDTO.setCity_name((String) row[5]);
	            measurementInfoDTO.setRegion_name((String) row[6]);
	            measurementInfoDTO.setCountry_name((String) row[7]);
	            measurementInfoDTO.setRecord_saved_at((String) row[8]);
	            measurementInfoDTO.setRecord_modified_at((String) row[9]);
	            measurementInfoDTO.setIs_record_active((Boolean) row[10]);
	            
	            // Inicializamos la lista de registros aquí si es nula
	            measurementInfoDTO.setRecords(new ArrayList<>()); 
	            
	            measurementMap.put(measurementId, measurementInfoDTO);
	        }

	        // Ahora mapeamos los detalles de sensor_data
	        SensorDataInfoDTO sensorDataInfoDTO = new SensorDataInfoDTO();
	        sensorDataInfoDTO.setCorrelative((Integer) row[11]);
	        sensorDataInfoDTO.setMetric_id((Integer) row[12]);
	        sensorDataInfoDTO.setMetric_name((String) row[13]);
	        sensorDataInfoDTO.setMetric_value((Double) row[14]);
	        sensorDataInfoDTO.setDatetime_epoch((Long) row[15]);
	        sensorDataInfoDTO.setDatetime_legible(Instant.ofEpochSecond((Long) row[15]));

	        // Añadimos el detalle al conjunto de registros
	        measurementInfoDTO.getRecords().add(sensorDataInfoDTO);
	    }

	    // Devolvemos la lista de MeasurementInfoDTO, eliminando duplicados
	    return new ArrayList<>(measurementMap.values());
	}

	
	public MeasurementDTO fromJsonToDTO(String json) {
        try {
            return objectMapper.readValue(json, MeasurementDTO.class);
        } catch (Exception e) {        	
            throw new com.tld.exception.InvalidJsonFormatException("Error al convertir JSON a MeasurementDTO", e);
        }
    }
	
	

	
	
	

}
