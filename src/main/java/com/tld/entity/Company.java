package com.tld.entity;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="company")
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="company_id")
	private Long companyId;	
	
	@Column(name="company_name", nullable = false, unique=true)
	private String companyName;	
	
	//@Column(name="company_api_key", nullable = false)
	//private String companyApiKey;	
	 @Column(nullable = false, unique = true)
	 private String companyApiKey;
	 //private String companyApiKey = UUID.randomUUID().toString();
	
	@ManyToOne
    @JoinColumn(name = "company_created_by")	
	private Users companyCreatedBy;
	
	@Column(name="company_created_at",updatable = false, nullable = false)
	private Instant companyCreatedAt;
	
	@Column(name="company_is_active", nullable = false)
	private Boolean companyIsActive;
	
	@ManyToOne
	@JoinColumn(name="company_modified_by")
	private Users companyModifiedBy;
	
	@Column(name="company_modified_at")
	private Instant companyModifiedAt;
	
	@PrePersist
    protected void onCreate() {
        this.companyCreatedAt = Instant.now(); // Se asigna al crear
        this.companyModifiedAt = Instant.now(); // También se asigna para evitar nulos
        this.companyIsActive = true;
        this.companyApiKey=generateRandomApiKey();
    }

    @PreUpdate
    protected void onUpdate() {
        this.companyModifiedAt = Instant.now(); // Solo se actualiza en update
    }
	
	
	public Company(Long companyId) {		
		this.companyId=companyId;		
	}
	
	public Company(String companyName, String companyApiKey) {		
		this.companyName=companyName;	
		this.companyApiKey=companyApiKey;
	}

    private String generateRandomApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 32 bytes para una clave de 64 caracteres en Base64
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
	
	


}


