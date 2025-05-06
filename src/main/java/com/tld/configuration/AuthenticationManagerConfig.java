package com.tld.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //Indica que esta clase es una clase de configuración de Spring.
public class AuthenticationManagerConfig {
	
	private final UserDetailsService userDetailsService; //Servicio que obtiene los detalles de los usuarios desde la base de datos
    private final PasswordEncoder passwordEncoder; // Encargado de codificar y verificar las contraseñas 

   
    public AuthenticationManagerConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    //AuthenticationManagerBuilder para configurar la autenticación en Spring Security.
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	//UserDetailsService para que Spring pueda obtener los usuarios desde la base de datos.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); //Asigna el PasswordEncoder para que las contraseñas se comparen correctamente al autenticarse.
    }
}
