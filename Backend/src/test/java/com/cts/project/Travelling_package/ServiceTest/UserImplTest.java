package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Security.JwtUtil;
import com.cts.project.Travelling_package.Service.ServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userServiceImpl.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<User> result = userServiceImpl.getUserById(1);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        Optional<User> result = userServiceImpl.getUserById(2);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean created = userServiceImpl.createUser(user);
        assertTrue(created);
        assertEquals("USER", user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUser_UsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        boolean created = userServiceImpl.createUser(user);
        assertFalse(created);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateHotelManager_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean created = userServiceImpl.createHotelManager(user);
        assertTrue(created);
        assertEquals("HOTELMANAGER", user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateHotelManager_UsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        boolean created = userServiceImpl.createHotelManager(user);
        assertFalse(created);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateTravelAgent_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean created = userServiceImpl.createTravelAgent(user);
        assertTrue(created);
        assertEquals("TRAVELAGENT", user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateTravelAgent_UsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        boolean created = userServiceImpl.createTravelAgent(user);
        assertFalse(created);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyInt())).thenReturn("jwtToken");

        String token = userServiceImpl.authenticateUser("testuser", "password");
        assertEquals("jwtToken", token);
    }

    @Test
    void testAuthenticateUser_WrongPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "password")).thenReturn(false);

        String token = userServiceImpl.authenticateUser("testuser", "wrong");
        assertNull(token);
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());
        String token = userServiceImpl.authenticateUser("nouser", "password");
        assertNull(token);
    }

    @Test
    void testUpdateUser_Found() {
        User updated = new User();
        updated.setUsername("newuser");
        updated.setEmail("new@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userServiceImpl.updateUser(1, updated);
        assertNotNull(result);
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        User updated = new User();
        User result = userServiceImpl.updateUser(2, updated);
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1);
        userServiceImpl.deleteUser(1);
        verify(userRepository).deleteById(1);
    }
}
