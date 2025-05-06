package com.tld.configuration.jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationFilterTest {
	 @Mock
	    private JwtUtils jwtUtils;

	    @Mock
	    private UserDetailsService userDetailsService;

	    @InjectMocks
	    private JwtAuthenticationFilter jwtAuthenticationFilter;

	    @Mock
	    private HttpServletRequest request;

	    @Mock
	    private HttpServletResponse response;

	    @Mock
	    private FilterChain filterChain;

	    private final String validToken = "valid.token.value";

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testDoFilterInternal_validToken() throws ServletException, IOException {
	        String validToken = "valid.token.value";

	        // Simula la solicitud
	        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
	        when(jwtUtils.validateToken(validToken)).thenReturn(true);
	        when(jwtUtils.getUsernameFromToken(validToken)).thenReturn("testUser");
	        
	        // Simula UserDetailsService para devolver un UserDetails simulado
	        UserDetails userDetails = mock(UserDetails.class);
	        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

	        // Simula el método getAuthentication
	        when(jwtUtils.getAuthentication(anyString(), any(UserDetails.class)))
	                .thenReturn(new UsernamePasswordAuthenticationToken("testUser", null, new ArrayList<>()));

	        // Llama al filtro
	        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

	        // Verifica que la autenticación esté configurada
	        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
	        assertEquals("testUser", SecurityContextHolder.getContext().getAuthentication().getName());

	        // Verifica que filterChain.doFilter se haya llamado
	        verify(filterChain).doFilter(request, response);
	    }



	    @Test
	    public void testExtractJwtFromRequest_validToken() throws Exception {
	        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

	        // Usar reflexión para invocar el método privado
	        Method method = JwtAuthenticationFilter.class.getDeclaredMethod("extractJwtFromRequest", HttpServletRequest.class);
	        method.setAccessible(true); // Hacer el método accesible

	        String token = (String) method.invoke(jwtAuthenticationFilter, request);

	        assertNotNull(token);
	        assertEquals(validToken, token);
	    }

	    @Test
	    public void testExtractJwtFromRequest_noToken() throws Exception {
	        when(request.getHeader("Authorization")).thenReturn(null);

	      
	        Method method = JwtAuthenticationFilter.class.getDeclaredMethod("extractJwtFromRequest", HttpServletRequest.class);
	        method.setAccessible(true); 

	        String token = (String) method.invoke(jwtAuthenticationFilter, request);

	        assertNull(token);
	    }
	    
	    @Test
	    public void testExtractJwtFromRequest_usingReflection() throws Exception {
	        String validToken = "valid.token.value";
	        
	        // Mock the request
	        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

	       
	        Method method = JwtAuthenticationFilter.class.getDeclaredMethod("extractJwtFromRequest", HttpServletRequest.class);
	        method.setAccessible(true);  

	       
	        String token = (String) method.invoke(jwtAuthenticationFilter, request);

	      
	        assertNotNull(token);
	        assertEquals(validToken, token);
	    }
	   
}
