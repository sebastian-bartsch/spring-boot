package com.tld.service;

import java.util.List;
import com.tld.dto.CompanyDTO;
import com.tld.dto.info.CompanyInfoDTO;

public interface CompanyService {
	
	CompanyInfoDTO addCompany(CompanyDTO companyDTO);	
	CompanyInfoDTO updateCompany(Long companyId, CompanyDTO companyDTO) ;
	List<CompanyInfoDTO>getCompanies(String field, String value);
	CompanyInfoDTO deleteCompany(Long companyId);

}
