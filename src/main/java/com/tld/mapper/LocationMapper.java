package com.tld.mapper;

import org.springframework.stereotype.Component;

import com.tld.dto.LocationDTO;
import com.tld.entity.City;
import com.tld.entity.Company;
import com.tld.entity.Location;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class LocationMapper {
	
	public LocationDTO toDTO(Location location) {			
		return new LocationDTO(location.getLocationId(),location.getCompany().getCompanyId(), location.getLocationAddress(), location.getCity().getCityId(),
				location.getLocationMeta());		
	}
	
	//
	public  Location toEntity(LocationDTO locationDTO) {		
		return new Location (new Company (locationDTO.getCompanyId()), 
							locationDTO.getLocationAddress(), 
							new City(locationDTO.getCityId()),				
							locationDTO.getLocationMeta());
		
	}

}
