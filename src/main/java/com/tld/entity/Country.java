package com.tld.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name="country")
public class Country {
	
	@Id	
	@Column(name="country_id")
	private Integer countryId;	
	
	@Column(name="country_name", nullable = false, unique = true)
	private String countryName;
	
	@Column(name="country_is_active", nullable = false)
	private Boolean countryIsActive;
	
	@Column(name="country_created_at", updatable = false, nullable = false)
	private Instant countryCreatedAt;
	

	@Column(name="country_modified_at")
	private Instant countryModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.countryCreatedAt = Instant.now(); // Se asigna al crear
        this.countryModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.countryModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
	
	
	public Country(Integer countryId) {
		this.countryId=countryId;
	}

}
