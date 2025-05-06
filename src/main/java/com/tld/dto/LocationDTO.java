package com.tld.dto;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationDTO {
	
	private Long locationId;
	private Long companyId;
	private String locationAddress;	
	private Integer cityId;
	private String locationMeta;

}
