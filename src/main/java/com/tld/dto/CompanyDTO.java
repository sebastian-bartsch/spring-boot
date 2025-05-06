package com.tld.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyDTO {
	
	private Long companyId;
    private String companyName;
    private String companyApiKey;
}
