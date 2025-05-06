package com.tld.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tld.dto.MeasurementDTO;
import com.tld.dto.SensorDataDTO;
import com.tld.dto.info.MeasurementInfoDTO;
import com.tld.entity.Company;
import com.tld.entity.Measurement;
import com.tld.entity.Metric;
import com.tld.entity.Sensor;
import com.tld.entity.SensorData;
import com.tld.jpa.repository.CompanyRepository;
import com.tld.jpa.repository.MeasurementRepository;
import com.tld.jpa.repository.MetricRepository;
import com.tld.jpa.repository.SensorDataRepository;
import com.tld.jpa.repository.SensorRepository;
import com.tld.mapper.MeasurementMapper;
import com.tld.service.MeasurementService;
import com.tld.util.LogUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeasurementServiceImpl implements MeasurementService{

	final SensorDataRepository sensorDataRepository;
	final SensorRepository sensorRepository;
	final CompanyRepository companyRepository;
	final MeasurementRepository measurementRepository;
	final MetricRepository metricRepository;
	final MeasurementMapper measurementMapper;
	
	private final JdbcTemplate jdbcTemplate;
	
	
	@Override
	@Transactional
	public void addSensorData(MeasurementDTO measurementDTO, String sensorApiKey) {
		
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl addSensorData");		
		
		if(measurementDTO.getApi_key()==null) {
			if(sensorApiKey==null || sensorApiKey.isEmpty() ) {				
				throw new com.tld.exception.InvalidApiKeyException ("Solicitud enviada sin sensor Api key");
			}else {
				measurementDTO.setApi_key(sensorApiKey);
			}
		}		
		
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud tiene apikey "+measurementDTO.getApi_key()+" se busca entidad.");
	
		final Sensor sensor = sensorRepository.findBySensorApiKey(measurementDTO.getApi_key())
						.orElseThrow(() -> new com.tld.exception.EntityNotFoundException("Sensor / Api key no encontrado. No se grabarán metricas recibidas"));		
		
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Entidad Sensor encontrada, id: "+sensor.getSensorId()+" nombre: "+sensor.getSensorName());
				
		saveMeasurementDataUsingHibernateAndJdbc(measurementDTO, sensor);	
		
	}
	
	@Override
	public void addSensorDataRabbit(MeasurementDTO measurementDTO) {
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl addSensorDataRabbit");	
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "sensor API KEY "+measurementDTO.getApi_key());	
		final Sensor sensor = sensorRepository.findBySensorApiKey(measurementDTO.getApi_key())
				.orElseThrow(() -> new com.tld.exception.EntityNotFoundException("Sensor / Api key no encontrado. No se grabarán metricas recibidas"));		
		saveMeasurementDataUsingHibernateAndJdbc(measurementDTO, sensor);		
	}

	

	@Override
	public MeasurementInfoDTO updateSensorData (String sensorApiKey, Long measurementId, String companyApiKey) {
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl updateSensorData");		
		
		final Company company= getCompanyByApiKey(companyApiKey);
		
		final Measurement measurement = measurementRepository.findById(measurementId)
				.orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No existe registro para poder inactivar"));
		
		if (measurement.getSensor() == null) {
		    throw new com.tld.exception.EntityNotFoundException("El sensor asociado a la medición no existe");
		}
		
		if(sensorRepository.findIfCompanyAndSensorAreOk(company.getCompanyId(), measurement.getSensor().getSensorId())==0) {
			throw new com.tld.exception.InvalidCompanySensorException("Solo puedes modificar cuando tu compania y el sensor estan relacionados");
		}			

		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Seteamos el estado a activo, id: "+measurementId);	
		measurement.setMeasurementIsActive(true);
		measurementRepository.save(measurement);		
		//Se piden todos los valores porque al ser sqls genericos validamos que exista relacion entre los parametros entregados (para un posible metodo get)
		return measurementMapper.mapToMeasurementInfoDTO( measurementRepository.findMeasurementById(measurementId,sensorApiKey,companyApiKey));
	}


	@Override
	public List<MeasurementInfoDTO> getSensorDataByEpoch(Long from, Long to, String companyApiKey) {
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl getSensorDataByEpoch");
		//Validar que el header sea valido
		getCompanyByApiKey(companyApiKey);
		
		//Valido la relacion entre company y sensor en los sqls gracias a los joins entre tablas		
		List<Object[]> info = measurementRepository.findMeasurementDataByEpoch(from, to, companyApiKey);
		if(info.isEmpty()) {
			throw new com.tld.exception.EntityNotFoundException("No hay resultados para la compania");
		}
		return measurementMapper.mapToListMeasurementInfoDTO(info);
	}

	@Override
	public List<MeasurementInfoDTO> getSensorDataByCompany(String companyApiKey) {
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl getSensorDataByCompany");
		//Validar que el header sea valido
		getCompanyByApiKey(companyApiKey);
		List<Object[]> info = measurementRepository.findMeasurementDataByCompany(companyApiKey);
		if(info.isEmpty()) {
			throw new com.tld.exception.EntityNotFoundException("No hay resultados para la compania");
		}
		return measurementMapper.mapToListMeasurementInfoDTO(info);
	}

	@Override
	public MeasurementInfoDTO getSensorDataById(Long measurementID, String sensorApiKey, String companyApiKey) {	
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl getSensorDataById");
		//Validar que el header sea valido
		getCompanyByApiKey(companyApiKey);
		List<Object[]> info = measurementRepository.findMeasurementById(measurementID, sensorApiKey, companyApiKey);
		if(info.isEmpty()) {
			throw new com.tld.exception.EntityNotFoundException("No hay resultados para la compania / sensor");
		}
		return measurementMapper.mapToMeasurementInfoDTO(info);
	}

	@Override
	public MeasurementInfoDTO deleteSensorData(String sensorApiKey, Long measurementId, String companyApiKey) {
		//Es igual al updateSensorData solo que inactiva registro. Para cumplir con requerimientos lo hice separado
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Solicitud recibida en impl updateSensorData");		
		
		final Company company= getCompanyByApiKey(companyApiKey);
		
		final Measurement measurement = measurementRepository.findById(measurementId)
				.orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No existe registro para poder inactivar"));
		
		if (measurement.getSensor() == null) {
		    throw new com.tld.exception.EntityNotFoundException("El sensor asociado a la medición no existe");
		}
		
		if(sensorRepository.findIfCompanyAndSensorAreOk(company.getCompanyId(), measurement.getSensor().getSensorId())==0) {
			throw new com.tld.exception.InvalidCompanySensorException("Solo puedes modificar cuando tu compania y el sensor estan relacionados");
		}			

		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Seteamos el estado a activo, id: "+measurementId);	
		measurement.setMeasurementIsActive(false);
		measurementRepository.save(measurement);		
		//Se piden todos los valores porque al ser sqls genericos validamos que exista relacion entre los parametros entregados (para un posible metodo get)
		return measurementMapper.mapToMeasurementInfoDTO( measurementRepository.findMeasurementById(measurementId,sensorApiKey,companyApiKey));
	}
	
	
	private Company getCompanyByApiKey(String apiKey) {
		LogUtil.log(SensorServiceImpl.class, Level.INFO, "Validando companyApiKey: "+apiKey);
	    return companyRepository.findByCompanyApiKey(apiKey)
	        .orElseThrow(() -> new com.tld.exception.InvalidApiKeyException("No existe la compañía con la API key entregada: " + apiKey));
	}

	
	
	private void saveMeasurementDataUsingHibernateAndJdbc(MeasurementDTO measurementDTO, Sensor sensor) {
		
		Measurement measurement = new Measurement();
		measurement.setSensor(sensor);		
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Entidad Measurement creado y seteado con");
		
		Long measurementId = measurementRepository.save(measurement).getMeasurementId();
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Measurement insertado, id: "+measurementId);	
	
		String sql = "INSERT INTO sensor_data (measurement_id, sensor_data_correlative, metric_id, sensor_value, sensor_data_created_at) VALUES (?, ?, ?, ?, ?)";
		int correlativo = 1;
		for(int i=0; i < measurementDTO.getJson_data().size(); i++ ) {			
			SensorDataDTO sensorDataDTO = measurementDTO.getJson_data().get(i);			
			Map<String, Object> sensorDataMap = sensorDataDTO.getSensorData();			
			for (String key : sensorDataMap.keySet()) {
			    String metricName = key;
			    LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Buscando metrica con nombre "+metricName);
			    Metric metric = metricRepository.findByMetricName(metricName)
			            .orElseGet(() -> {
			                Metric newMetric = new Metric();
			                newMetric.setMetricName(metricName);
			                LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Se grabo metrica, id: "+newMetric.getMetricId());
			                return metricRepository.save(newMetric);
			            });				   
			 jdbcTemplate.update(sql, measurementId, correlativo++, metric.getMetricId(),  (Double) sensorDataMap.get(metricName), sensorDataDTO.getDatetime());
		     LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Sensor_data agregado a batch con JdbcTemplate ("+ measurementId+", "+(correlativo-1)+", "+metric.getMetricId()+", "+(Double) sensorDataMap.get(metricName)+", "+sensorDataDTO.getDatetime()+") ");		
			}
		}	
	}
	
	private void saveMeasurementDataUsingHibernate(MeasurementDTO measurementDTO, Sensor sensor) {
		Measurement measurement = new Measurement();
		measurement.setSensor(sensor);				
		Long measurementId = measurementRepository.save(measurement).getMeasurementId();
		LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Measurement insertado, id: "+measurementId);		
			
		List<SensorData> sensorDataList = new ArrayList<>();		
		int correlativo = 1;
		for(int i=0; i < measurementDTO.getJson_data().size(); i++ ) {			
			SensorDataDTO sensorDataDTO = measurementDTO.getJson_data().get(i);			
			Map<String, Object> sensorDataMap = sensorDataDTO.getSensorData();
			
			for (String key : sensorDataMap.keySet()) {
			    String metricName = key;
			    LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Buscando metrica con nombre "+metricName);
			    Metric metric = metricRepository.findByMetricName(metricName)
			            .orElseGet(() -> {
			                Metric newMetric = new Metric();
			                newMetric.setMetricName(metricName);
			                LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Se grabo metrica, id: "+newMetric.getMetricId());
			                return metricRepository.save(newMetric);
			            });					 			    
			    
			    LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "creando entidad sensor_data");
			    SensorData sensorData = new SensorData(
			    	measurement, //padre
			    	correlativo ++, //correlativo 
			        metric, //Va la entidad metric y ahi esta id y nombre
			        (Double) sensorDataMap.get(metricName),  //valores de la metrica
			        sensorDataDTO.getDatetime()  //valor Long fecha epoch que envian en el json
			    );
			    sensorDataList.add(sensorData);
			    LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Sensor_data agregado a lista");			    
			}
		}			
	  LogUtil.log(MeasurementServiceImpl.class, Level.INFO, "Lista sensorData guardada");	
	  sensorDataRepository.saveAll(sensorDataList); 		

	}

	
}
