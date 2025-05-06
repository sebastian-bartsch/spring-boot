package com.tld.mapper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tld.dto.SensorDataDTO;
import com.tld.entity.Metric;
import com.tld.entity.SensorData;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorDataMapper {	

	public SensorDataDTO toDTO(SensorData sensorData) {
        SensorDataDTO sensorDataDTO = new SensorDataDTO();
        sensorDataDTO.setDatetime(sensorData.getSensorDataDateTime());

        // Crear el mapa de métricas dinámicas
        Map<String, Object> sensorDataMap = new HashMap<>();
        
        if (sensorData.getMetric() != null) {
            sensorDataMap.put(sensorData.getMetric().getMetricName(), sensorData.getSensorDataValue());
        }

        sensorDataDTO.setSensorData(sensorDataMap);
        return sensorDataDTO;
        
    }

    public List<SensorData> toEntityList(SensorDataDTO sensorDataDTO) {
    	 List<SensorData> sensorDataList = new ArrayList<>();
         Long datetime = sensorDataDTO.getDatetime();

         if (sensorDataDTO.getSensorData() != null) {
             for (Map.Entry<String, Object> entry : sensorDataDTO.getSensorData().entrySet()) {
                 String metricName = entry.getKey();
                 Double metricValue = entry.getValue() instanceof Number ? ((Number) entry.getValue()).doubleValue() : null;

                 SensorData sensorData = new SensorData();
                 sensorData.setMetric(new Metric(metricName)); // Crear métrica con el nombre
                 sensorData.setSensorDataValue(metricValue);
                 sensorData.setSensorDataDateTime(datetime);

                 sensorDataList.add(sensorData);
             }
         }

         return sensorDataList;
    }
	/*  public static SensorDataDTO toDTO(SensorData sensorData) {
	        return new SensorDataDTO(sensorData.getSensorDataId().getSensorApiKey(),sensorData.getSensorDataId().getSensorCorrelative(),
	        		sensorData.getSensorEntry(), sensorData.getSensorDataIsActive() );	        
	    }

	    public static SensorData toEntity(SensorDataDTO sensorDataDTO) {	    	
	    	return new SensorData(sensorDataDTO.getSensorApiKey(),sensorDataDTO.getSensorDataCorrelative(),sensorDataDTO.getSensorDataEntry());	
	    }
*/
}
