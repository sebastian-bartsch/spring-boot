package com.tld.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tld.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUserName(String userName);
	
	boolean existsByUserName(String userName);  // Método para verificar si ya existe un nombre de usuario
	
}
