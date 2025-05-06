package com.tld.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tld.dto.info.CompanyInfoDTO;
import com.tld.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	 Optional<Company> findByCompanyApiKey(String companyApiKey);
	 
	 
	 @Query(value = """	 		
	 	   SELECT * FROM get_active_companies(:field, :value)					
		    """, nativeQuery = true)
	 List<CompanyInfoDTO> findCompanies(@Param("field") String field ,@Param("value") String value);	 
	 

}
