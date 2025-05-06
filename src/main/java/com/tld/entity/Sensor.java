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
@Table (name="sensor", uniqueConstraints = @UniqueConstraint(columnNames = {"location_id", "sensor_name"}))
public class Sensor {	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sensor_id")
	private Long sensorId;	
	
	@ManyToOne
    @JoinColumn(name = "location_id", nullable = false)	
	private Location location;	
	
	@Column(name="sensor_name", nullable = false)
	private String sensorName;	
	
	@ManyToOne
    @JoinColumn(name = "category_id", nullable = false)	
	private Category category;
	
	@Column(name="sensor_meta", nullable = false)
	private String sensorMeta;
	
	@Column(name="sensor_api_key", nullable = false, unique = true )
	private String sensorApiKey;	
	
	@Column(name="sensor_created_at", updatable = false, nullable = false)
	private Instant sensorCreatedAt;
	
	@Column(name="sensor_is_active", nullable = false)
	private Boolean sensorIsActive;
	
	@Column(name="sensor_modified_at")
	private Instant sensorModifiedAt;	
	
	@PrePersist
    protected void onCreate() {
        this.sensorCreatedAt = Instant.now(); // Se asigna al crear
        this.sensorModifiedAt = Instant.now(); // También se asigna para evitar nulos
        this.sensorIsActive=true;        
        this.sensorApiKey=generateRandomApiKey();
    }

    @PreUpdate
    protected void onUpdate() {
        this.sensorModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
	

	public Sensor (Long sensorId) {
		this.sensorId=sensorId;
	}
	
	public Sensor (String sensorApiKey) {
		this.sensorApiKey=sensorApiKey;
	}
	
	
	public Sensor(Location location, String sensorName, Category category, String sensorMeta, String sensorApiKey) {
		this.location=location;
		this.sensorName=sensorName;
		this.category=category;
		this.sensorMeta=sensorMeta;
		this.sensorApiKey=sensorApiKey;		
	}
	
    private String generateRandomApiKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 32 bytes para una clave de 64 caracteres en Base64
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

	
	
		
}
