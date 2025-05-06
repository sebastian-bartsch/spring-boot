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
import org.springframework.util.StringUtils;

import com.tld.dto.CompanyDTO;
import com.tld.dto.info.CompanyInfoDTO;
import com.tld.entity.Company;
import com.tld.entity.Users;
import com.tld.jpa.repository.CompanyRepository;
import com.tld.jpa.repository.UserRepository;
import com.tld.mapper.CompanyMapper;
import com.tld.service.CompanyService;
import com.tld.util.LogUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{
		
    final CompanyRepository  companyRepository;
    final UserRepository userRepository;
    final CompanyMapper companyMapper;    
    
	@Override
	public CompanyInfoDTO addCompany(CompanyDTO companyDTO) {	    
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no valido"));   
		LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Solicitud de: "+user.getUserName()+" recibida en impl addCompany");
		Company company= companyMapper.toEntity(companyDTO);
		company.setCompanyCreatedBy(user);
		company.setCompanyModifiedBy(user);
		
	    final Company savedCompany;
		try {
			savedCompany = companyRepository.save(company);
		} catch (DataIntegrityViolationException e) {
			String message = e.getMostSpecificCause().getMessage().toLowerCase();
			if (message.contains("llave duplicada") || message.contains("duplicate key")) {
		        throw new com.tld.exception.UniqueConstraintViolationException("No se puede ingresar mas de una vez el mismo nombre de compania.");
		    }
		    throw new com.tld.exception.CustomDatabaseException("Error de integridad de datos", e);
		}    	
    	LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Se retorna DTO con compania almacenada");
    	
    	return companyMapper.toInfoDTO(savedCompany);
	}
	
	@Override
	public List<CompanyInfoDTO> getCompanies(String field, String value) {			
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no valido"));
		LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Solicitud de: "+user.getUserName()+" recibida en impl getCompanies");
		final List<CompanyInfoDTO> resultado= companyRepository.findCompanies(field, value);		
		if (resultado.isEmpty()) {
			throw new com.tld.exception.EntityNotFoundException("No hay resultados con el campo: "+field+" y valor: "+value);
		}		
		return resultado;
	}
	
	@Override
	public CompanyInfoDTO updateCompany(Long companyId, CompanyDTO companyDTO) {
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no valido"));
		LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Solicitud de: "+user.getUserName()+" recibida en impl updateCompany");
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new com.tld.exception.EntityNotFoundException("Company no encontrada con ID: " + companyId));
			 
	    company.setCompanyModifiedBy(user);	
		
		if(StringUtils.hasText(companyDTO.getCompanyName())) {
			company.setCompanyName(companyDTO.getCompanyName());
		}		
		if(StringUtils.hasText(companyDTO.getCompanyApiKey())){			
			company.setCompanyApiKey(companyDTO.getCompanyApiKey());			
		}	

		company.setCompanyIsActive(true);		
		final Company savedCompany = companyRepository.save(company);
		
		return companyMapper.toInfoDTO(savedCompany);
	}	

	@Override
	public CompanyInfoDTO deleteCompany(Long companyId) {		
		final Users user = getAuthenticatedUser().orElseThrow(() -> new com.tld.exception.InvalidUserException("Usuario no valido"));
		LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Solicitud de: "+user.getUserName()+" recibida en impl deleteCompany");
		
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new com.tld.exception.EntityNotFoundException("Company no encontrada con ID: " + companyId));
		
		if(!company.getCompanyIsActive()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(ZoneId.of("America/Santiago"));
			throw new com.tld.exception.CustomDatabaseException("El registro ya esta inactivo, fue hecho por "+company.getCompanyModifiedBy().getUserName()+
																" a las "+formatter.format(company.getCompanyModifiedAt()));			
		}
		company.setCompanyIsActive(false);		     
	    company.setCompanyModifiedBy(user);	    
	    final Company savedCompany = companyRepository.save(company);	
	    return companyMapper.toInfoDTO(savedCompany);
	}		
	 	

	private Optional<Users> getAuthenticatedUser() {
		LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Validando usuario");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            LogUtil.log(CompanyServiceImpl.class, Level.INFO, "Usuario "+userName+" validado.");
            return userRepository.findByUserName(userName);
        }
        LogUtil.log(CompanyServiceImpl.class, Level.WARNING, "Validacion de usuario fallida.");
        return Optional.empty();
    }
	

	 
	
	
}
