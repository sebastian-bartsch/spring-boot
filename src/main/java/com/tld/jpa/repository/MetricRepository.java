package com.tld.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tld.entity.Metric;

public interface MetricRepository extends JpaRepository<Metric, Integer> {
	
	Optional<Metric> findByMetricName(String metricName);

}
