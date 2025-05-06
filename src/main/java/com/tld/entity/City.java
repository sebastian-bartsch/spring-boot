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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name="city")
public class City {
	//comentario de prueba
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="city_id")
	private Integer cityId;	
	
	@Column(name="city_name", nullable = false, unique = true)
	private String cityName;	
	
	@ManyToOne
    @JoinColumn (name="region_id", nullable = false)
	private Region region;
	
	@Column(name="city_is_active", nullable = false)
	private Boolean cityIsActive;
	
	@Column(name="city_created_at", updatable = false, nullable = false)
	private Instant cityCreatedAt;
	

	@Column(name="city_modified_at")
	private Instant cityModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.cityCreatedAt = Instant.now(); // Se asigna al crear
        this.cityModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.cityModifiedAt = Instant.now(); // Solo se actualiza en update
    }
    
	
	public City(Integer cityId) {
		this.cityId=cityId;
	}
	
	
}
