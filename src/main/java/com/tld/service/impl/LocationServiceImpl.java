package com.tld.service.impl;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tld.dto.LocationDTO;
import com.tld.dto.info.LocationInfoDTO;
import com.tld.entity.City;
import com.tld.entity.Location;
import com.tld.entity.Users;
import com.tld.jpa.repository.UserRepository;
import com.tld.jpa.repository.CityRepository;
import com.tld.jpa.repository.LocationRepository;
import com.tld.mapper.LocationMapper;
import com.tld.service.LocationService;
import com.tld.util.LogUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{
	
	final LocationRepository locationRepository;
	final CityRepository cityRepository;
	final UserRepository userRepository;
	final LocationMapper locationMapper;	

	@Override
	public LocationInfoDTO addLocation(LocationDTO locationDTO) {			
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no valido"));   
		LogUtil.log(LocationServiceImpl.class, Level.INFO, "Solicitud recibida de usuario: "+user.getUserName()+"en impl addLocation");
	    
		Location location= locationMapper.toEntity(locationDTO);					    
		location.setLocationCreatedBy(user);
	    location.setLocationModifiedBy(user);		    
	    
	    try {	    	
	    	final Location savedLocation = locationRepository.save(location);
	    	LogUtil.log(LocationServiceImpl.class, Level.INFO, "Retornando location almacenada");   
			return    locationRepository.findLocations("id", savedLocation.getLocationId().toString())
					    	            .stream()
					    	            .findFirst()
					    	            .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("No se encontro la location recien almacenada"));	
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage().toLowerCase();
			if (message.contains("llave duplicada") || message.contains("duplicate key")) {
		        throw new com.tld.exception.UniqueConstraintViolationException("No se puede ingresar la misma direccion mas de una vez por compania.");
		    }
		    throw new com.tld.exception.CustomDatabaseException("Error de integridad de datos", e);			
		}	
	 
	}

	
	@Override
	public LocationInfoDTO updateLocation(Long locationId, LocationDTO locationDTO) {		
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no válido"));
		LogUtil.log(LocationServiceImpl.class, Level.INFO, "Solicitud de usuario: "+user.getUserName()+" recibida en impl updateLocation");
			
		Location location = locationRepository.findById(locationId)
			   .orElseThrow(() -> new com.tld.exception.EntityNotFoundException("Location no encontrada con ID: " + locationId));		
		
		Optional.ofNullable(locationDTO.getLocationAddress()).ifPresent(location::setLocationAddress);		
		Optional.ofNullable(locationDTO.getCityId()).ifPresent(cityId -> {
	        City city = cityRepository.getReferenceById(cityId);
	        location.setCity(city);
	    });		
		Optional.ofNullable(locationDTO.getLocationMeta()).ifPresent(location::setLocationMeta);
	
		
		location.setLocationIsActive(true);
		location.setLocationModifiedBy(user);
		
		locationRepository.save(location);		
		
		//get(0) para no retonar una lista con 1 elemento. 
		return locationRepository.findLocations("id",location.getLocationId().toString()).get(0) ;		
				
	}	

	@Override
	public List<LocationInfoDTO> getLocations(String field, String value) {
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no válido"));
		LogUtil.log(LocationServiceImpl.class, Level.INFO, "Solicitud de usuario: "+user.getUserName()+" recibida en impl getLocations");
		return  locationRepository.findLocations(field, value);		
	}
	
	@Override
	public LocationInfoDTO deleteLocation(Long locationId) {		
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no válido"));		
		LogUtil.log(LocationServiceImpl.class, Level.INFO, "Solicitud de usuario: "+user.getUserName()+"recibida en impl deleteLocation");
		Location location = locationRepository.findById(locationId)
				   .orElseThrow(() -> new EntityNotFoundException("Location no encontrada con ID: " + locationId));		  
		
		if(!location.getLocationIsActive()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.of("America/Santiago"));
			throw new com.tld.exception.CustomDatabaseException("El registro ya esta inactivo, fue hecho por "+location.getLocationModifiedBy().getUserName()+
																" a las "+formatter.format(location.getLocationModifiedAt()));
		}		
		location.setLocationIsActive(false);
	    location.setLocationModifiedBy(user);	
		locationRepository.save(location);
		return locationRepository.findLocations("id",location.getLocationId().toString()).get(0) ;
	}
	
	 private Optional<Users> getAuthenticatedUser() {
		LogUtil.log(LocationServiceImpl.class, Level.INFO, "Validando usuario");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            LogUtil.log(LocationServiceImpl.class, Level.INFO, "Usuario "+userName+" validado.");
            return userRepository.findByUserName(userName);
        }
        LogUtil.log(LocationServiceImpl.class, Level.WARNING, "Validacion de usuario fallida.");
        return Optional.empty();
    }

}
