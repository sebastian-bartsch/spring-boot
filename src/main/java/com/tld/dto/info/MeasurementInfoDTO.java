package com.tld.dto.info;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MeasurementInfoDTO {
	
	private Long id;
	private String sensor_api_key;
	private String sensor_name;
    private String company_name;    
    private String location_adress;
    private String city_name;
    private String region_name;
    private String country_name;
    private String record_saved_at;  
    private String record_modified_at;
    private Boolean is_record_active;
    private List<SensorDataInfoDTO> records;
    
	

	

}
