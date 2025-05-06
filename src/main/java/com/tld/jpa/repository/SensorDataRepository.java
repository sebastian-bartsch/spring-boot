package com.tld.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tld.dto.info.SensorDataInfoDTO;
import com.tld.entity.SensorData;
import com.tld.model.id.SensorDataId;

public interface SensorDataRepository extends JpaRepository<SensorData,SensorDataId>{
	//Sql base para todos los metodos get
	String querySql="""
			 			 select sensor_data_correlative, sensor.sensor_api_key, 
			  			  sensor.sensor_id, company_name, sensor_name, 
				  		  sensor_entry, TO_CHAR(sensor_data_created_at, 'DD-MM-YYYY HH24:MI')::VARCHAR(255) AS sensor_data_created_at,
				  		  TO_CHAR(sensor_data_modified_at, 'DD-MM-YYYY HH24:MI')::VARCHAR(255) AS sensor_data_modified_at,
					      sensor_data_is_active from sensor_data
						  join sensor on
						  sensor_data.sensor_api_key =sensor.sensor_api_key 
						  join location on
						  sensor.location_id =location.location_id
						  join company on
						  location.company_id=company.company_id						
					""";
	
	
	

	
		  @Query(value = querySql+"""
				  		 WHERE
						 company.company_api_key= :companyApiKey AND 
						 sensor_data_correlative = :correl AND 
						 sensor.sensor_api_key =:sensorApiKey ;		  		
		  		""", nativeQuery = true)
		 List<SensorDataInfoDTO> findSensorDataById(@Param("correl") Long correl, @Param("sensorApiKey") String sensorApiKey, @Param("companyApiKey") String companyApiKey);
		  
		  
		  @Query(value =querySql+"""		  
					  where
					  company.company_api_key=:companyApiKey and
				      sensor_data.sensor_data_created_at  between  to_timestamp(:from ::BIGINT) and to_timestamp(:to ::BIGINT); 
			  		""", nativeQuery = true)
		  List<SensorDataInfoDTO> findSensorDataByEpoch(@Param("from") Long from, @Param("to") Long to, @Param("companyApiKey") String companyApiKey);
		  
		  @Query(value =querySql+"""		  
				  where
				  company.company_api_key=:companyApiKey ;
		  	  	""", nativeQuery = true)
		  List<SensorDataInfoDTO> findSensorDataByCompany(@Param("companyApiKey") String companyApiKey);
		  
		  
		  @Query(value = """
		 		SELECT count(*) FROM sensor
				join location on
				sensor.location_id=location.location_id
				join company on
				location.company_id= company.company_id
				where
				company_api_key= :companyApiKey and
				sensor_api_key=:sensorApiKey ;			
			    """, nativeQuery = true)
		 Short findIfCompanyAndSensorAreOk(@Param("companyApiKey") String companyApiKey, @Param("sensorApiKey") String sensorApiKey);	
	
}
