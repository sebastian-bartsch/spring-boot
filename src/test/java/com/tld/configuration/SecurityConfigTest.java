package com.tld.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc  
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    

    @Test
    public void testPrivateRoutesWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/location"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());  
    }

   
    // Test de PasswordEncoder
    @Test
    public void testPasswordEncoding() {
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Verifica que la contraseña codificada no sea igual a la contraseña en texto plano
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    // Test del AuthenticationManager
    @Test
    public void testAuthenticationManager() {
        assertNotNull(authenticationManager); 
    }
}
