package com.tld.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.tld.entity.Permission;
import com.tld.entity.Role;
import com.tld.entity.Users;
import com.tld.jpa.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@SpringBootTest
public class UserDetailsServiceImplTest {
	 @Mock
	    private UserRepository userRepository;

	    @InjectMocks
	    private UserDetailsServiceImpl userDetailsService;

	    private Users user;
	    private Role role;
	    private Permission permission;

	    @BeforeEach
	    void setUp() {
	        permission = new Permission();
	        permission.setPermissionName("Actualizar");

	        role = new Role();
	        role.setRoleName("ROLE_administrador");
	        role.setPermissions(Set.of(permission));

	        user = new Users();
	        user.setUserName("Luis");
	        user.setUserPassword("123456");
	        user.setUserEnabled(true);
	        user.setAccountNonExpired(true);
	        user.setCredentialsNonExpired(true);
	        user.setAccountNonLocked(true);
	        user.setRole(Set.of(role));
	    }

	    @Test
	    void testLoadUserByUsername_UserExists() {
	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertEquals("Luis", userDetails.getUsername());
	        assertEquals("123456", userDetails.getPassword());

	        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_administrador")));
	        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("Actualizar")));

	        verify(userRepository).findByUserName("Luis");
	    }

	    @Test
	    void testLoadUserByUsername_UserNotFound() {
	        when(userRepository.findByUserName("inexistente")).thenReturn(Optional.empty());

	        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
	            userDetailsService.loadUserByUsername("inexistente");
	        });

	        assertEquals("Usuario no encontrado: inexistente", exception.getMessage());
	        verify(userRepository).findByUserName("inexistente");
	    }
	    
	    @Test
	    void testLoadUserByUsername_MultipleRolesAndPermissions() {
	        Permission writePermission = new Permission();
	        writePermission.setPermissionName("WRITE_PRIVILEGES");

	        Role userRole = new Role();
	        userRole.setRoleName("ROLE_USER");
	        userRole.setPermissions(Set.of(writePermission));

	        Role adminRole = new Role();
	        adminRole.setRoleName("ROLE_administrador");  
	        adminRole.setPermissions(Set.of(writePermission));

	        user.setRole(Set.of(userRole, adminRole));  

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertEquals("Luis", userDetails.getUsername());
	        assertEquals("123456", userDetails.getPassword());

	        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_administrador")));  
	        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("WRITE_PRIVILEGES"))); 
	        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
	    }
	    
	    @Test
	    void testLoadUserByUsername_UserDisabled() {
	        user.setUserEnabled(false);

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertFalse(userDetails.isEnabled()); 
	    }
	    
	    @Test
	    void testLoadUserByUsername_AccountExpired() {
	        user.setAccountNonExpired(false);

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertFalse(userDetails.isAccountNonExpired()); 
	    }
	    
	    @Test
	    void testLoadUserByUsername_CredentialsExpired() {
	        user.setCredentialsNonExpired(false);

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertFalse(userDetails.isCredentialsNonExpired()); 
	    }
	    
	    @Test
	    void testLoadUserByUsername_AccountLocked() {
	        user.setAccountNonLocked(false);

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertFalse(userDetails.isAccountNonLocked());
	    }
	    
	    @Test
	    void testLoadUserByUsername_NoRolesOrPermissions() {
	        user.setRole(new HashSet<>());

	        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

	        UserDetails userDetails = userDetailsService.loadUserByUsername("Luis");

	        assertNotNull(userDetails);
	        assertEquals("Luis", userDetails.getUsername());
	        assertEquals("123456", userDetails.getPassword());

	        assertTrue(userDetails.getAuthorities().isEmpty());
	    }
	    @Test
	    void testLoadUserByUsername_UsernameNotFoundException() {
	        when(userRepository.findByUserName("NonExistentUser")).thenReturn(Optional.empty());

	        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
	            userDetailsService.loadUserByUsername("NonExistentUser");
	        });

	        assertEquals("Usuario no encontrado: NonExistentUser", exception.getMessage());
	    }
}
