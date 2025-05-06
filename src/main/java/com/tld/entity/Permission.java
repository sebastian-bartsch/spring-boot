package com.tld.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "permission")
public class Permission {
	
	@Id	
	@Column(name = "permission_id")
	private Integer permissionId;
	
	@Column(name = "permission_name")
	private String permissionName;
		
	@Column(name="permission_created_at", updatable = false, nullable = false)
	private Instant permissionCreatedAt;
	

	@Column(name="permission_modified_at")
	private Instant permissionModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.permissionCreatedAt = Instant.now(); // Se asigna al crear
        this.permissionModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.permissionModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
	
	@ManyToMany(mappedBy = "permissions")
	private Set<Role> role;

}
