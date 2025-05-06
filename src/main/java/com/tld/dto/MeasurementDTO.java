package com.tld.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MeasurementDTO {
	
	private Long measurementId;	
	private String api_key;	
	private Long sensorId;	
	private List<SensorDataDTO> json_data;
	private Boolean measurementIsActive;
}
