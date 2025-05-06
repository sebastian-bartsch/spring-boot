package com.tld.service;

import java.util.List;

import com.tld.dto.SensorDTO;
import com.tld.dto.info.SensorInfoDTO;


public interface SensorService {
	
	//SensorInfoDTO addSensor (SensorDTO sensorDTO);	
	SensorInfoDTO addSensor (SensorDTO sensorDTO, String companyApiKey);
	SensorInfoDTO updateSensor(String companyApiKey, SensorDTO sensorDTO) ;	
	List<SensorInfoDTO> getSensors(String field, String value, String companyApiKey);
	SensorInfoDTO deleteSensor(Long  sensorId, String companyApiKey);

	//Usado para ingresar data en sensorDATA
//	Optional<SensorDTO> getSensorByApiKey(String companyApiKey);
	
}
