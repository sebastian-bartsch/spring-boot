package com.tld.mapper;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.tld.dto.CompanyDTO;
import com.tld.dto.info.CompanyInfoDTO;
import com.tld.entity.Company;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.of("America/Santiago"));
	public CompanyDTO toDTO(Company company) {
        return new CompanyDTO(company.getCompanyId(), company.getCompanyName(), company.getCompanyApiKey());
    }
	
	
	public Company toEntity (CompanyDTO companyDTO) {
        return new Company(companyDTO.getCompanyName(), companyDTO.getCompanyApiKey());    
        
    }
	
	public CompanyInfoDTO toInfoDTO(Company company) {
		    return new CompanyInfoDTO(
		        company.getCompanyId(),
		        company.getCompanyName(),
		        company.getCompanyApiKey(),
		        company.getCompanyCreatedBy().getUserName(),
		        formatter.format(company.getCompanyCreatedAt()),
		        company.getCompanyModifiedBy().getUserName(),
		        formatter.format(company.getCompanyModifiedAt()),
		        company.getCompanyIsActive()
		    );
	 }
	 
}
