package com.tld.dto.info;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInfoDTO {
	
    private Long companyId;
    private String companyName;
    private String companyApiKey;
    private String userNameC;  
    private String companyCreatedAt;
    private String userNameM;  
    private String companyModifiedAt;
    private Boolean isCompanyActive;

}
