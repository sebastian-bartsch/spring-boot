package com.tld.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tld.configuration.jwt.JwtUtils;
import com.tld.entity.Role;
import com.tld.entity.Users;
import com.tld.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_Success() throws Exception {
        String username = "testUser";
        String password = "testPass";

        Users user = new Users();
        user.setUserName(username);
        user.setUserEnabled(true);

        Role role = new Role();
        role.setRoleName("ROLE_USER");

        
        user.setRole(Collections.singleton(role)); 

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtUtils.createToken(auth)).thenReturn("mockedJwtToken");

        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name").value(username))
                .andExpect(jsonPath("$.user.userEnabled").value(true))
                .andExpect(jsonPath("$.user.role[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }


    @Test
    void testLogin_Failure() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        mockMvc.perform(post("/api/auth/login")
                        .param("username", "wrongUser")
                        .param("password", "wrongPass"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Error de autenticación: Invalid credentials"));
    }

  

    @Test
    void testRegisterUser_UsernameExists() throws Exception {
        Users user = new Users();
        user.setUserName("existingUser");

        doThrow(new IllegalArgumentException("Usuario ya existe")).when(userService).registerUser(any());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"El nombre de usuario ya está en uso.\"}"));
    }

    @Test
    void testGetUser_Success() throws Exception {
        String token = "Bearer validToken";
        String username = "testUser";

        Users user = new Users();
        user.setUserName(username);
        user.setUserEnabled(true);
        Role role = new Role();
        role.setRoleName("ROLE_USER");

     
        user.setRole(Collections.singleton(role));

        when(jwtUtils.getUsernameFromToken("validToken")).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/user")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.name").value(username))
                .andExpect(jsonPath("$.user.userEnabled").value(true))
                .andExpect(jsonPath("$.user.role[0]").value("ROLE_USER"));
    }


 
    @Test
    void testChangePassword_Success() throws Exception {
        String token = "Bearer validToken";
        String username = "testUser";
        Users user = new Users();
        user.setUserName(username);

        when(jwtUtils.getUsernameFromToken("validToken")).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        doNothing().when(userService).updatePassword(any(), any());

        mockMvc.perform(post("/api/auth/change-password")
                        .param("username", username)
                        .param("newPassword", "newSecret")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña actualizada con éxito."));
    }

    @Test
    void testChangePassword_Forbidden() throws Exception {
        when(jwtUtils.getUsernameFromToken("validToken")).thenReturn("anotherUser");

        mockMvc.perform(post("/api/auth/change-password")
                        .param("username", "testUser")
                        .param("newPassword", "newSecret")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("No puedes cambiar la contraseña de otro usuario."));
    }

    @Test
    void testChangePassword_UserNotFound() throws Exception {
        when(jwtUtils.getUsernameFromToken("validToken")).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/change-password")
                        .param("username", "testUser")
                        .param("newPassword", "newSecret")
                        .header("Authorization", "Bearer validToken"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado."));
    }

   
}