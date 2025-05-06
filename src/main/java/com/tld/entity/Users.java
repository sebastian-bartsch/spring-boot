package com.tld.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="users")
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Integer userId;	
	
	@Column(name="user_name", nullable = false)
	private String userName;	
	
	@Column(name="user_password", nullable = false)
	private String userPassword;	
	
	@Column(name = "user_enabled")
	private boolean userEnabled;
	
//	@ManyToOne
//    @JoinColumn(name = "role_id", nullable = false)	
//	private Role role;
	
	@Column(name = "account_non_expired")
	private boolean accountNonExpired;
	
	@Column(name = "account_non_locked")
	private boolean accountNonLocked;
	
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired;
	

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	        name="users_role",
	        joinColumns= @JoinColumn(name="user_id"),
	        inverseJoinColumns=
	            @JoinColumn(name="role_id")
	    )
	private Set<Role> role;	

	@Column(name="user_created_at", updatable = false, nullable = false)
	private Instant userCreatedAt;
	

	@Column(name="user_modified_at")
	private Instant userModifiedAt;
	
	
	@PrePersist
    protected void onCreate() {
        this.userCreatedAt = Instant.now(); // Se asigna al crear
        this.userModifiedAt = Instant.now(); // También se asigna para evitar nulos
        this.userEnabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.userModifiedAt = Instant.now(); // Solo se actualiza en update
    }   
    

	public Users(Integer userId) {
	    this.userId = userId;
	}
	
	
	
}
