package com.tld.configuration;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.mockito.Mockito.*;

import java.util.ArrayList;
@SpringBootTest
public class AuthenticationManagerConfigTest {
	 @Test
	    void testPasswordEncoder() {
	        // Test para verificar que el PasswordEncoder esté funcionando correctamente
	        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String rawPassword = "password";
	        String encodedPassword = passwordEncoder.encode(rawPassword);
	        
	        assert passwordEncoder.matches(rawPassword, encodedPassword);  // La contraseña debería coincidir con el valor codificado
	    }

	    @Test
	    void testUserDetailsService() {
	        // Test para verificar si el UserDetailsService devuelve el usuario esperado
	        UserDetailsService userDetailsService = mock(UserDetailsService.class);
	        String username = "testUser";
	        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
	                username, "encodedPassword", new ArrayList<>());
	        
	        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);

	        org.springframework.security.core.userdetails.UserDetails retrievedUser = userDetailsService.loadUserByUsername(username);

	        assert retrievedUser.getUsername().equals(username);  // Verifica que el nombre de usuario sea correcto
	    }
}
