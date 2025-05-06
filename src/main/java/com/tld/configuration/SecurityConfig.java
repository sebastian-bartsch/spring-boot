package com.tld.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.tld.configuration.jwt.JwtAuthenticationFilter;

import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.List;

@Configuration //Indica que esta clase proporciona configuraciones a la aplicación
@EnableWebSecurity // Habilita la seguridad en la aplicación con Spring Security.
public class SecurityConfig {
	
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 
	 public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
	        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    }
	 
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (opcional)
	            .authorizeHttpRequests(auth -> auth
	            		.requestMatchers(
	            	            "/swagger-ui/**",
	            	            "/v3/api-docs/**",
	            	            "/swagger-ui.html",
	            	            "/v3/api-docs",
	            	            "/swagger-resources/**",
	            	            "/webjars/**"
	            	        ).permitAll()
	                .requestMatchers("/api/auth/login", "/api/v1/sensordata/**", "/api/v1/sensor/**","/api/v1/rabbit/**","/api/v1/measurement/**").permitAll() // Endpoints públicos
	                //.requestMatchers("/swagger-ui.html", "/swagger-ui/", "/v3/api-docs/", "/swagger-resources/", "/webjars/").permitAll()
	                //.requestMatchers("/api/location", "/api/auth/register").hasRole("ADMINISTRADOR") // Solo administradores
	                .requestMatchers("/api/location", "/api/auth/register").hasAuthority("ROLE_administrador")
	                .anyRequest().authenticated() // Todo lo demás requiere autenticación
	            )
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Agrega filtro JWT

	        return http.build();
	    }
	
    
    //customAthenticationSuccessHandler / AuthenticationSuccessHandler personalizado.
    //Cuando el usuario se autentica correctamente, simplemente se devuelve un HTTP 200 OK
    @Bean
    AuthenticationSuccessHandler customAthenticationSuccessHandler() {
    	return new AuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_OK);
			}
		};
    }
    
    //AuthenticationManager el cual se obtiene de la configuracion de autenticacion
    //Es responsable de manejar la autenticación de usuarios.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}

  //PasswordEncoder
  //Cuando un usuario se registra, su contraseña se almacena de forma segura en la base de datos con BCrypt.
  @Bean
  PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); //BCryptPasswordEncoder para codificar y comparar contraseñas de forma segura.
	}
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowCredentials(true);
      configuration.setAllowedOrigins(List.of("*")); // Permitir todos los orígenes temporalmente
      configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Permitir Angular
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "company_api_key"));

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }
  
}
