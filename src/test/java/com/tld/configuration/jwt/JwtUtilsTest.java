package com.tld.configuration.jwt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {
	
	 private JwtUtils jwtUtils;

	    @BeforeEach
	    void setUp() throws NoSuchFieldException, IllegalAccessException {
	        jwtUtils = new JwtUtils();
	        
	        // Usar reflexión para acceder y modificar los campos privados
	        Field privateKeyField = JwtUtils.class.getDeclaredField("PRIVATE_KEY");
	        privateKeyField.setAccessible(true);
	        privateKeyField.set(jwtUtils, "secretkeyforjwtwhichis256bitlong!!");  // Asignar clave correcta

	        Field userGeneratorField = JwtUtils.class.getDeclaredField("USER_GENERATOR");
	        userGeneratorField.setAccessible(true);
	        userGeneratorField.set(jwtUtils, "testUserGenerator");
	    }
 

	    @Test
	    void testValidateToken_invalidToken() {
	       
	        String invalidToken = "invalid.token";
	        assertFalse(jwtUtils.validateToken(invalidToken));
	    }

	   
}
