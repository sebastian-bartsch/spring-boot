package com.tld.dto.info;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SensorDataInfoDTO {		
	private Integer correlative;
	private Integer metric_id;
	private String metric_name;	
    private Double metric_value;
    private Long datetime_epoch;
    private Instant datetime_legible;
	
}

