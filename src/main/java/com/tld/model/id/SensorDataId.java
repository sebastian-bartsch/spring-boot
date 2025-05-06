package com.tld.model.id;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SensorDataId implements Serializable {
	
	private static final long serialVersionUID = 1L;

    
    private Long measurementId;   
    private Integer sensorDataCorrelative;
    private Integer metricId;  // <-- Se agrega metricId

    public SensorDataId() {}

    public SensorDataId(Long measurementId, Integer sensorCorrelative, Integer metricId) {
        this.measurementId = measurementId;
        this.sensorDataCorrelative = sensorCorrelative;
        this.metricId = metricId;  // <-- Se inicializa metricId
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDataId that = (SensorDataId) o;
        return Objects.equals(measurementId, that.measurementId) &&
               Objects.equals(sensorDataCorrelative, that.sensorDataCorrelative) &&
               Objects.equals(metricId, that.metricId);  // <-- Se añade la comparación de metricId
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurementId, sensorDataCorrelative, metricId);  // <-- Se añade metricId al hash
    }
}
