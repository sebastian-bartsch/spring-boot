package com.tld.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.tld.configuration.jwt.JwtUtils;
import com.tld.entity.Role;
import com.tld.entity.Users;
import com.tld.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") //Permite solicitudes desde cualquier origen (CORS habilitado globalmente).
@Tag(name = "Auth Controller", description = "Gestion de Usuarios")
public class AuthController {
	
	 	@Autowired
	    private AuthenticationManager authenticationManager; // autenticar a los usuarios.


	    @Autowired
	    private UserService userService; //registrar usuarios y gestionar credenciales.
	    
	    @Autowired
	    private JwtUtils jwtUtils; // Para generar y validar tokens JWT.
	    
	    
	    // Recibe el username y password y devuelve un JWT en la respuesta.
	    @PostMapping("/login")
	    @Operation(summary = "Autenticación de usuario", description = "Permite a un usuario autenticarse con sus credenciales y recibir un token JWT.")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json")),
	        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
	        @ApiResponse(responseCode = "500", description = "Error en el servidor")
	    })
	    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
	        try {
	            Authentication authentication = authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(username, password));

	            SecurityContextHolder.getContext().setAuthentication(authentication); // Asigna el usuario autenticado

	            Users user = userService.findByUsername(username)
	                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	            // Obtener roles en formato de lista de Strings
	            List<String> roles = user.getRole().stream()
	                    .map(Role::getRoleName) // Asegúrate de que este es el getter correcto
	                    .collect(Collectors.toList());

	            // Generar el token JWT
	            String token = jwtUtils.createToken(authentication);

	            // Crear la respuesta en JSON
	            Map<String, Object> response = new HashMap<>();
	            Map<String, Object> userData = new HashMap<>();
	            
	            userData.put("name", user.getUserName());
	            userData.put("userEnabled", user.isUserEnabled());
	            userData.put("role", roles);

	            response.put("user", userData);
	            response.put("token", token);

	            return ResponseEntity.ok(response);
	         
	        } catch (AuthenticationException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación: " + e.getMessage());
	        }
	    }


	    @PostMapping("/register")
	    @Operation(summary = "Registro de usuario", description = "Permite a un administrador registrar un nuevo usuario.")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Usuario registrado con éxito"),
	        @ApiResponse(responseCode = "400", description = "El nombre de usuario ya está en uso"),
	        @ApiResponse(responseCode = "403", description = "No autorizado para registrar usuarios"),
	        @ApiResponse(responseCode = "500", description = "Error en el servidor")
	    })
	    public ResponseEntity<?> registerUser(@RequestBody Users user) {
	        try {
	            // Intentar registrar el usuario
	            userService.registerUser(user); // Esto ya incluye la verificación si el usuario existe

	            // Si todo va bien, respondemos con un mensaje de éxito
	            return ResponseEntity.status(HttpStatus.CREATED)
	                                 .body("{\"message\":\"Usuario registrado correctamente\"}");
	        } catch (IllegalArgumentException e) {
	            // Si el nombre de usuario ya está en uso, la excepción será capturada aquí
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                                 .body("{\"message\":\"El nombre de usuario ya está en uso.\"}");
	        } catch (Exception e) {
	            // Capturar cualquier otra excepción para casos inesperados
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("{\"message\":\"Error interno al registrar usuario.\"}");
	        }
	    }
	    

	    @GetMapping("/user")
	    @Operation(summary = "Obtener información del usuario autenticado", description = "Retorna el nombre de usuario y sus roles si el token es válido.")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Usuario autenticado"),
	        @ApiResponse(responseCode = "401", description = "Token inválido o expirado"),
	        @ApiResponse(responseCode = "403", description = "No tienes permisos para acceder")
	    })
	    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
	        if (token == null || !token.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente.");
	        }

	        token = token.substring(7); // Quitar "Bearer "

	        try {
	            // Obtener el username desde el token
	            String username = jwtUtils.getUsernameFromToken(token);

	            // Buscar usuario en la BD
	            Users user = userService.findByUsername(username)
	                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

	            // Obtener roles en formato de lista de Strings
	            List<String> roles = user.getRole().stream()
	                    .map(Role::getRoleName)
	                    .collect(Collectors.toList());

	            // Crear la respuesta en JSON
	            Map<String, Object> response = new HashMap<>();
	            Map<String, Object> userData = new HashMap<>();
	            
	            userData.put("name", user.getUserName());
	            userData.put("userEnabled", user.isUserEnabled());
	            userData.put("role", roles);

	            response.put("user", userData);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado.");
	        }
	    }

	    
	    @PostMapping("/change-password")
	    @Operation(summary = "Cambio de contraseña", description = "Permite a un usuario cambiar su contraseña si está autenticado.")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Contraseña actualizada con éxito"),
	        @ApiResponse(responseCode = "403", description = "No puedes cambiar la contraseña de otro usuario"),
	        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
	        @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
	    })
	    public ResponseEntity<String> changePassword(@RequestParam String username, @RequestParam String newPassword, @RequestHeader("Authorization") String token) {
	        if (token == null || !token.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o ausente.");
	        }

	        token = token.substring(7); // Quitar el prefijo "Bearer "

	        try {
	            String loggedInUsername = jwtUtils.getUsernameFromToken(token);  // Obtener el username desde el token
	            if (!username.equals(loggedInUsername)) {
	                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes cambiar la contraseña de otro usuario.");
	            }

	            Optional<Users> user = userService.findByUsername(username);
	            if (user.isPresent()) {
	                userService.updatePassword(user.get(), newPassword);
	                return ResponseEntity.ok("Contraseña actualizada con éxito.");
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado.");
	        }
	    }

	
}
