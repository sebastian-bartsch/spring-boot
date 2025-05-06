package com.tld.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tld.entity.Users;
import com.tld.jpa.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;



@SpringBootTest
public class UserServiceImplTest {
	
	@Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Users user;
    
    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserName("Luis");
        user.setUserPassword("123456");
    }
    
    @Test
    void testRegisterUser() {
        user.setUserPassword("plaintextpassword"); 

        when(passwordEncoder.encode("plaintextpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getUserPassword());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    void testFindByUsername_UserExists() {
        when(userRepository.findByUserName("Luis")).thenReturn(Optional.of(user));

        Optional<Users> foundUser = userService.findByUsername("Luis");

        assertTrue(foundUser.isPresent());
        assertEquals("Luis", foundUser.get().getUserName());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUserName("sebastian")).thenReturn(Optional.empty());

        Optional<Users> foundUser = userService.findByUsername("sebastian");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdatePassword() {
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(Users.class))).thenReturn(user);

        userService.updatePassword(user, "newpassword");

        assertEquals("encodedNewPassword", user.getUserPassword());
        verify(userRepository).save(user);
    }
}
