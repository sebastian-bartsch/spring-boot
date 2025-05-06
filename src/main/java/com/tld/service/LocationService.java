package com.tld.service;

import java.util.List;
import com.tld.dto.LocationDTO;
import com.tld.dto.info.LocationInfoDTO;



public interface LocationService {		
	
	LocationInfoDTO addLocation(LocationDTO locationDTO);	
	LocationInfoDTO updateLocation(Long locationId, LocationDTO locationDTO) ;	
	List<LocationInfoDTO> getLocations(String field, String value);
	LocationInfoDTO deleteLocation(Long locationId);
}
