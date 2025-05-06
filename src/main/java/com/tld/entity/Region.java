package com.tld.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table (name="region")
public class Region {
	
	@Id	
	@Column(name="region_id")
	private Integer regionId;	
	
	@Column(name="region_name", nullable = false, unique = true)
	private String regionName;	

	@ManyToOne
    @JoinColumn(name = "country_id", nullable = false)	
	private Country country;
	
	
	@Column(name="region_is_active", nullable = false)
	private Boolean regionIsActive;
	
	@Column(name="region_created_at", updatable = false, nullable = false)
	private Instant regionCreatedAt;
	

	@Column(name="region_modified_at")
	private Instant regionModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.regionCreatedAt = Instant.now(); // Se asigna al crear
        this.regionModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.regionModifiedAt = Instant.now(); // Solo se actualiza en update
    }   

    public Region(Integer regionId) {
		this.regionId=regionId;
	}
	
	
}
