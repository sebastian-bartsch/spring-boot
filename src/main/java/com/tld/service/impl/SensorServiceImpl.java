package com.tld.service.impl;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tld.dto.SensorDTO;
import com.tld.dto.info.SensorInfoDTO;
import com.tld.entity.Category;
import com.tld.entity.Company;
import com.tld.entity.Location;
import com.tld.entity.Sensor;
import com.tld.jpa.repository.CategoryRepository;
import com.tld.jpa.repository.CompanyRepository;
import com.tld.jpa.repository.LocationRepository;
import com.tld.jpa.repository.SensorRepository;
import com.tld.jpa.repository.UserRepository;
import com.tld.mapper.SensorMapper;
import com.tld.service.SensorService;
import com.tld.util.LogUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService{
	
	final SensorRepository sensorRepository;
	final LocationRepository locationRepository;
	final CompanyRepository companyRepository;
	final UserRepository userRepository;
	final CategoryRepository categoryRepository;
	final SensorMapper sensorMapper;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.of("America/Santiago"));
	
	 
	@Override
	public SensorInfoDTO addSensor(SensorDTO sensorDTO, String companyApiKey) {
		
		final Company company = getCompanyByApiKey(companyApiKey);		
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Solicitud recibida de company: "+company.getCompanyName()+"en impl addSensor");
		
		if (locationRepository.findIfLocationAndCompanyAreOk(company.getCompanyId(),sensorDTO.getLocationId())<1){
			throw new com.tld.exception.EntityNotFoundException("No existe tal direccion asociada a la compañia, no se ingresara sensor");
		}
		
		categoryRepository.findById(sensorDTO.getCategoryId())
				   .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No existe la categoria ingresada, id:" + sensorDTO.getCategoryId()));  		
		
		final Sensor sensor=sensorMapper.toEntity(sensorDTO);		
		
		
		Long id= sensorRepository.save(sensor).getSensorId();  
		if(id>0) {			
			return sensorRepository.findSensorById(id);					
		}else {
			throw new com.tld.exception.EntityNotFoundException("No se pudo grabar, informar a soporte.");
		}		
		
	}

	@Override
	public SensorInfoDTO updateSensor(String companyApiKey, SensorDTO sensorDTO) {	

		final Company company = getCompanyByApiKey(companyApiKey);		
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Solicitud recibida de company: "+company.getCompanyName()+"en impl updateSensro");
		
		
		final Sensor sensor= sensorRepository.findById(sensorDTO.getSensorId())
				   .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No existe sensor entregado en JSON, no se actualizara")); 	
	
			
		//validar que sensor y company esten relacionados
		if(sensorRepository.findIfCompanyAndSensorAreOk(company.getCompanyId(),sensor.getSensorId())==0) {
			throw new com.tld.exception.InvalidCompanySensorException("No existe relacion entre compania (api key) y sensor (json)");
		}
		
		if(sensorDTO.getLocationId() != null && sensorDTO.getLocationId() > 0) {
			if (locationRepository.findIfLocationAndCompanyAreOk(company.getCompanyId(),sensorDTO.getLocationId())<1){
				throw new com.tld.exception.EntityNotFoundException("No existe tal direccion asociada a la compañia, no se ingresara sensor");
			}else {
				sensor.setLocation(new Location(sensorDTO.getLocationId()));
			}			
		}
		
		if(sensorDTO.getCategoryId() != null && sensorDTO.getCategoryId() > 0) {
			Category category=categoryRepository.findById(sensorDTO.getCategoryId())
					   .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No existe la categoria ingresada, id:" + sensorDTO.getCategoryId())); 
			sensor.setCategory(category);
		}	
						
		//validacion: El json tiene una apikey?, ¿El apikey en json es igual al que ya tiene grabado?		
		String nuevaApiKey=sensorDTO.getSensorApiKey();		
		if(StringUtils.hasText(nuevaApiKey)&&(!nuevaApiKey.equals(sensor.getSensorApiKey()))){
			//Existe otro sensor con la apikey indicada en el json?
			Optional<Sensor> existeSensor = sensorRepository.findBySensorApiKey(sensorDTO.getSensorApiKey());
			if (existeSensor.isPresent()) {
			    throw new com.tld.exception.InvalidApiKeyException("No se puede actualizar el api key contenido en json ");
			} else {
			    sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
			}			
		}
		
		if(StringUtils.hasText(sensorDTO.getSensorName())) {
			sensor.setSensorName(sensorDTO.getSensorName());
		}
		
		sensor.setSensorIsActive(true);
		
		sensorRepository.save(sensor);
		
		//Retornar .get(0) para que no sea una lista de 1 elemento.
		return sensorRepository.findSensorById(sensor.getSensorId());
			
	}

	@Override
	public List<SensorInfoDTO> getSensors(String field, String value, String companyApiKey) {	
		final Company company = getCompanyByApiKey(companyApiKey);		
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Solicitud recibida de company: "+company.getCompanyName()+"en impl getSensors");
		//La validacion con company api key y sensores entregados ocurre directo en el sql. De no existir relacion entre sensores y company
		//retornará vacio
		
		List<SensorInfoDTO> resultado= sensorRepository.findSensors(field, value, companyApiKey);		
		if (resultado.isEmpty()) {
			throw new com.tld.exception.EntityNotFoundException("No hay resultados, 2 posibles razones: no existe o intentas ver informacion de otra compañia");
		}
		return resultado;
		
	}

	@Override
	public SensorInfoDTO deleteSensor(Long sensorId, String companyApiKey) {
		final Company company = getCompanyByApiKey(companyApiKey);		
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Solicitud recibida de company: "+company.getCompanyName()+"en impl deleteSensor");		
		
		final Sensor sensor= sensorRepository.findById(sensorId)
				   .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No puedes eliminar lo que nunca existio")); 
		
		//validar que sensor y company esten relacionados
		if(sensorRepository.findIfCompanyAndSensorAreOk(company.getCompanyId(),sensor.getSensorId())==0) {
			throw new com.tld.exception.InvalidCompanySensorException("No existe relacion entre compania (api key) y sensor (json)");
		}		
		
		sensor.setSensorIsActive(false);
		sensorRepository.save(sensor);		
		
		return sensorRepository.findSensorById(sensorId);
	}
		
	/*@Override
	public Optional<SensorDTO> getSensorByApiKey(String companyApiKey) {		
		return sensorRepository.findBySensorApiKey(companyApiKey).map(SensorMapper::toDTO);
	}*/

	private Company getCompanyByApiKey(String apiKey) {
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Validando companyApiKey: "+apiKey);
	    return companyRepository.findByCompanyApiKey(apiKey)
	        .orElseThrow(() -> new com.tld.exception.InvalidApiKeyException("No existe la compañía con la API key entregada: " + apiKey));
	}
		
}
