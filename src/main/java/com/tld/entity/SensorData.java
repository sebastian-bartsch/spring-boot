package com.tld.entity;

import com.tld.model.id.SensorDataId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sensor_data")
public class SensorData {
	
	
	@EmbeddedId
    private SensorDataId sensorDataId; 	
	
	@ManyToOne
	@MapsId("measurementId")
	@JoinColumn(name="measurement_id")
	private Measurement measurement;
	
	@ManyToOne
	@MapsId("metricId")
    @JoinColumn(name = "metric_id", nullable = false)	
	private Metric metric;  
	
    
	@Column(name="sensor_value", nullable = false)
	private Double sensorDataValue;		

	@Column(name="sensor_data_created_at", nullable = false)
	private Long sensorDataDateTime;
	
    // 
    public SensorData(Long measurementId, Integer sensorDataCorrelative, Integer metricId) {
        this.sensorDataId = new SensorDataId(measurementId, sensorDataCorrelative, metricId);       
    }
    
    public SensorData(Measurement measurement, Integer sensorDataCorrelative, Metric metric, Double sensorDataValue, Long sensorDataDateTime) {
		this.sensorDataId = new SensorDataId(measurement.getMeasurementId(), sensorDataCorrelative, metric.getMetricId());  // Crear el ID compuesto
		this.measurement=measurement;
		this.metric=metric;
	// Relación con la entidad Measurement
		this.sensorDataValue = sensorDataValue;  // Valor de los datos del sensor
		this.sensorDataDateTime = sensorDataDateTime;  // Timestamp del dato
	}
}

