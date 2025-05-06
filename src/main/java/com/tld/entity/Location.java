package com.tld.entity;
import java.time.Instant;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="location",  uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "city_id", "location_address"}))
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="location_id")
	private Long locationId;	
	
	@ManyToOne
    @JoinColumn(name = "company_id", nullable = false)	
	private Company company;	
	
	@Column(name="location_address", nullable = false)
	private String locationAddress;	
	
	@ManyToOne
    @JoinColumn(name = "city_id", nullable = false)	
	private City city;
	
	@Column(name="location_meta", nullable = false)
	private String locationMeta;
	
	@ManyToOne
	@JoinColumn(name="location_created_by", nullable = false)
	private Users locationCreatedBy;
	
	@Column(name="location_created_at", updatable = false, nullable = false)
	private Instant locationCreatedAt;
	
	@Column(name="location_is_active", nullable = false)
	private Boolean locationIsActive;
	
	@ManyToOne
	@JoinColumn(name="location_modified_by", nullable = false)
	private Users locationModifiedBy;
	
	@Column(name="location_modified_at")
	private Instant locationModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.locationCreatedAt = Instant.now(); // Se asigna al crear
        this.locationModifiedAt = Instant.now(); // También se asigna para evitar nulos
        this.locationIsActive=true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.locationModifiedAt = Instant.now(); // Solo se actualiza en update
    }
	
	
	
	public Location(Company company,String locationAdress, City city, String locationMeta) {		
		this.company=company;
		this.locationAddress=locationAdress;
		this.city=city;
		this.locationMeta=locationMeta;
	}
	
	
	public Location(Long locationId) {
		this.locationId=locationId;
	}
	
	
	
	
	
	
}
