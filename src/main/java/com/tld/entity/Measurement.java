package com.tld.entity;

import java.time.Instant;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="measurement")
public class Measurement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="measurement_id")
    private Long measurementId;
	
	@ManyToOne
	@JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;	 
	
	@OneToMany(mappedBy = "measurement", cascade = CascadeType.ALL)
    private List<SensorData> sensorDataList; 
	
	@Column(name="measurement_created_at", nullable = false)
	private Instant measurementCreatedAt;
	
	@Column(name="measurement_modified_at", nullable = false)
	private Instant measurementModifiedAt;
	
	@Column(name="measurement_is_active", nullable = false)
	private Boolean measurementIsActive;
	
	@PrePersist
    protected void onCreate() {
        this.measurementCreatedAt = Instant.now(); // Se asigna al crear
        this.measurementModifiedAt = Instant.now();
        this.measurementIsActive= true;
	}
	
	@PreUpdate
	protected void onUpdate() {
        this.measurementModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
	
	
    public Measurement(Long measurementId) {
        this.measurementId = measurementId;
    }
    
    public Measurement(Long measurementId, Sensor sensor, List<SensorData> sensorDataList, Boolean measurementIsActive) {
        this.measurementId = measurementId;
        this.sensor= sensor;
        this.sensorDataList=sensorDataList;
        this.measurementIsActive=measurementIsActive;
    }
	

}
