package com.tld.dto.info;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SensorInfoDTO {
	private Long sensorId;
	private String sensorName;
	private String sensor_api_key;
	private Long locationId;	
    private String locationAddress;
    private Long companyId;          
	private String companyName;
	private Long sensorTotalRecords;
    private String sensorCreatedAt;	
	private String sensorModifiedAt;
	private Boolean sensorIsActive;

}
