package com.tld.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tld.dto.CompanyDTO;
import com.tld.dto.info.CompanyInfoDTO;
import com.tld.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

public class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddCompany_Success() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Test Company");

        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        companyInfoDTO.setCompanyId(1L); 
        companyInfoDTO.setCompanyName("Test Company"); 

        when(companyService.addCompany(any(CompanyDTO.class))).thenReturn(companyInfoDTO);

        
        mockMvc.perform(post("/api/v1/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDTO)))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.companyId").value(1))  
                .andExpect(jsonPath("$.companyName").value("Test Company")); 
    }


    @Test
    void testUpdateCompany_Success() throws Exception {
        Long companyId = 1L;
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Updated Company");

        CompanyInfoDTO updatedCompany = new CompanyInfoDTO();
        updatedCompany.setCompanyId(companyId);
        updatedCompany.setCompanyName("Updated Company");

        when(companyService.updateCompany(eq(companyId), any(CompanyDTO.class))).thenReturn(updatedCompany);

        mockMvc.perform(put("/api/v1/company/{companyId}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companyId))  
                .andExpect(jsonPath("$.companyName").value("Updated Company"));  
    }


    @Test
    void testGetCompanies_Success() throws Exception {
        String field = "name";
        String value = "Test Company";

       
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        companyInfoDTO.setCompanyId(1L);  
        companyInfoDTO.setCompanyName("Test Company"); 

        
        when(companyService.getCompanies(field, value)).thenReturn(List.of(companyInfoDTO));

        
        mockMvc.perform(get("/api/v1/company")
                        .param("field", field)
                        .param("value", value))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$[0].companyId").value(1))  
                .andExpect(jsonPath("$[0].companyName").value("Test Company"));  
    }


    @Test
    void testDeleteCompany_Success() throws Exception {
        Long companyId = 1L;
        CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
        companyInfoDTO.setCompanyId(companyId);  
        companyInfoDTO.setCompanyName("Test Company");

        when(companyService.deleteCompany(companyId)).thenReturn(companyInfoDTO);

        mockMvc.perform(delete("/api/v1/company/{companyId}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(companyId))  
                .andExpect(jsonPath("$.companyName").value("Test Company"));
    }

}
