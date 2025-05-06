package com.tld.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tld.dto.info.SensorInfoDTO;
import com.tld.entity.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
	
	Optional<Sensor> findBySensorApiKey(String sensorApiKey);
	
	 @Query(value = """
	 			  select sensor.sensor_id, 
									    sensor_name, 
							            sensor.sensor_api_key ,
								  		sensor.location_id, 
										CONCAT(location_address, '', '', city_name, '', '', region_name, '', '', country_name)::VARCHAR(500) AS location_address, 
										location.company_id, 									    
										company_name,  		 
								  		COALESCE(measurement_summary.sensor_total_measurements, 0) AS sensor_total_measurements,
								  		TO_CHAR(sensor_created_at AT TIME ZONE 'America/Santiago', 'DD-MM-YYYY HH24:MI')::VARCHAR(255) AS sensor_created_at,							  		 
										TO_CHAR(sensor_modified_at AT TIME ZONE 'America/Santiago', 'DD-MM-YYYY HH24:MI')::VARCHAR(255) AS sensor_modified_at,
										sensor_is_active 		 
								  from sensor 
								  join location on
								  sensor.location_id =location.location_id
								  join city on
								  location.city_id=city.city_id
								  join region on
								  city.region_id =region.region_id
								  join country on
								  region.country_id =country.country_id
								  join company on
								  location.company_id=company.company_id
								  LEFT JOIN (
											    SELECT sensor_id, count(*) AS sensor_total_measurements
											    FROM measurement
												WHERE measurement_is_active=true
											    GROUP BY sensor_id
											) measurement_summary ON sensor.sensor_id = measurement_summary.sensor_id	
				WHERE
				sensor.sensor_id= :sensorId;					
		    """, nativeQuery = true)
	SensorInfoDTO findSensorById(@Param("sensorId") Long sensorId);		
	 
	 
	 @Query(value = """
	 		  select * from get_active_sensors(:field, :value, :companyApiKey)
	 		""", nativeQuery = true)
	 List<SensorInfoDTO> findSensors(@Param("field") String field ,@Param("value") String value, @Param("companyApiKey") String companyApiKey);

	 @Query(value = """
		 		SELECT count(*) FROM sensor
				join location on
				sensor.location_id=location.location_id
				join company on
				location.company_id= company.company_id
				where
				company.company_id= :companyId and
				sensor.sensor_id=:sensorId ;
			    """, nativeQuery = true)
	  Short findIfCompanyAndSensorAreOk(@Param("companyId") Long companyId, @Param("sensorId") Long sensorId);	
	 
	 
		
		

}
