package com.tld.entity;

import java.time.Instant;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table (name="category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="category_id")
	private Integer categoryId;	
	
	@Column(name="category_name", nullable = false, unique = true)
	private String categoryName;
	
	@Column(name="category_is_active", nullable = false)
	private Boolean categoryIsActive;
	
	@Column(name="category_created_at", updatable = false, nullable = false)
	private Instant categoryCreatedAt;
	

	@Column(name="category_modified_at")
	private Instant categoryModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.categoryCreatedAt = Instant.now(); // Se asigna al crear
        this.categoryModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.categoryModifiedAt = Instant.now(); // Solo se actualiza en update
    }
	
	
	public Category(Integer categoryId) {
		this.categoryId=categoryId;
	}

}
