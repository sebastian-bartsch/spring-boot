package com.tld.mapper;

import org.springframework.stereotype.Component;

import com.tld.dto.SensorDTO;
import com.tld.entity.Category;
import com.tld.entity.Location;
import com.tld.entity.Sensor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorMapper {
	
	public SensorDTO toDTO(Sensor sensor) {			
		return new SensorDTO(sensor.getSensorId(),sensor.getLocation().getLocationId(),sensor.getSensorName(),sensor.getCategory().getCategoryId(),
				sensor.getSensorMeta(), sensor.getSensorApiKey());
	}
	
	public Sensor toEntity(SensorDTO sensorDTO) {		
		return new Sensor(new Location (sensorDTO.getLocationId()), sensorDTO.getSensorName(),new Category(sensorDTO.getCategoryId()),
				sensorDTO.getSensorMeta(), sensorDTO.getSensorApiKey());	
	}	

}
