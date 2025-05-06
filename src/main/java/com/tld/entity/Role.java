package com.tld.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name="role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_id")
	private Integer roleId;	
	
	@Column(name="role_name", nullable = false, unique = true)
	private String roleName;
	
	@ManyToMany(mappedBy = "role")
	private Set<Users> users;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	        name="role_permission",
	        joinColumns= @JoinColumn(name="role_id"),
	        inverseJoinColumns=
	            @JoinColumn(name="permission_id")
	    )
	private Set<Permission> permissions;
	
	@Column(name="role_is_active", nullable = false)
	private Boolean roleIsActive;
	
	@Column(name="role_created_at", updatable = false, nullable = false)
	private Instant roleCreatedAt;
	

	@Column(name="role_modified_at")
	private Instant roleModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.roleCreatedAt = Instant.now(); // Se asigna al crear
        this.roleModifiedAt = Instant.now(); // También se asigna para evitar nulos
    }

    @PreUpdate
    protected void onUpdate() {
        this.roleModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
	
	
	public Role (Integer roleId) {
		this.roleId=roleId;
	}
	
}
