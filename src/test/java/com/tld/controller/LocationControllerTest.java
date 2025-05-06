package com.tld.controller;
import com.tld.dto.LocationDTO;
import com.tld.dto.info.LocationInfoDTO;
import com.tld.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocationControllerTest {
	 @InjectMocks
	    private LocationController locationController;

	    @Mock
	    private LocationService locationService;

	    private MockMvc mockMvc;
	    
	    private LocationInfoDTO locationInfoDTO;


	    @BeforeEach
	    public void setUp() {
	        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
	    }

	    @Test
	    void testAddLocation_Success() throws Exception {
	        LocationInfoDTO locationInfoDTO = new LocationInfoDTO();
	        locationInfoDTO.setLocationId(1L);
	        locationInfoDTO.setCompanyName("Iquique");  
	        locationInfoDTO.setLocationAddress("CalleX");
	        locationInfoDTO.setCityName("Talca"); 
	        locationInfoDTO.setLocationMeta("Meta");

	        when(locationService.addLocation(any(LocationDTO.class))).thenReturn(locationInfoDTO);

	        mockMvc.perform(post("/api/v1/location")
	                .contentType("application/json")
	                .content("{\"locationId\": 1, \"companyName\": \"Iquique\", \"locationAddress\": \"CalleX\", \"cityName\": \"Talca\", \"locationMeta\": \"Meta\"}"))
	                .andExpect(status().isOk())
	                .andExpect(content().json("{\"locationId\": 1, \"companyName\": \"Iquique\", \"locationAddress\": \"CalleX\", \"cityName\": \"Talca\", \"locationMeta\": \"Meta\"}"));
	    }

	    @Test
	    void testGetLocations_Success() throws Exception {
	        LocationInfoDTO locationInfoDTO = new LocationInfoDTO();
	        locationInfoDTO.setLocationId(1L);
	        locationInfoDTO.setLocationAddress("CalleX");
	        locationInfoDTO.setLocationMeta("Meta");
	        locationInfoDTO.setCompanyName("Iquique"); 
	        locationInfoDTO.setCityName("Talca"); 

	        when(locationService.getLocations("ciudad", "iquique")).thenReturn(List.of(locationInfoDTO));

	        mockMvc.perform(get("/api/v1/location")
	                .param("field", "ciudad")
	                .param("value", "iquique"))
	                .andExpect(status().isOk())
	                .andExpect(content().json("[{\"locationId\": 1, \"companyName\": \"Iquique\", \"locationAddress\": \"CalleX\", \"cityName\": \"Talca\", \"locationMeta\": \"Meta\"}]"));
	    }

	  


	    @Test
	    void testDeleteLocation_Success() throws Exception {
	        when(locationService.deleteLocation(1L)).thenReturn(locationInfoDTO);

	        mockMvc.perform(delete("/api/v1/location/1"))
	                .andExpect(status().isOk());
	         

	        verify(locationService).deleteLocation(1L);  
	    }


}
