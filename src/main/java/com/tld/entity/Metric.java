package com.tld.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
@Table(name="metric")
public class Metric {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="metric_id")
	private Integer metricId;
	
	@Column(name="metric_name")
	private String metricName;
	
	@Column(name="metric_created_at")
	private Instant metricCreatedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.metricCreatedAt = Instant.now(); // Se asigna al crear
    }

	
	public Metric(String metricName) {
		this.metricName=metricName;
		
	}
	
	
	
	
	
	
	
	
	
}
