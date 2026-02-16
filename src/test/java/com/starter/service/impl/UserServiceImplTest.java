package com.starter.service.impl;

import com.starter.convertor.EntityToResponseConvertor;
import com.starter.convertor.RequestToEntityConvertor;
import com.starter.dto.user.UserRequestDto;
import com.starter.dto.user.UserResponseDto;
import com.starter.entity.User;
import com.starter.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

	@MockBean
    private UserRepository userRepository;

	@MockBean
    private PasswordEncoder passwordEncoder;

    @Mock
    private RequestToEntityConvertor requestToEntityConvertor;

    @Mock
    private EntityToResponseConvertor entityToResponseConvertor;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(requestToEntityConvertor.convertUser(request, new User())).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(entityToResponseConvertor.convertUser(user)).thenReturn(new UserResponseDto("test@example.com"));

        UserResponseDto response = userService.createUser(request);

        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void testGetUserById_UserExists() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(entityToResponseConvertor.convertUser(user)).thenReturn(new UserResponseDto("test@example.com"));

        UserResponseDto response = userService.getUserById(1L);
        assertEquals("test@example.com", response.getEmail());
    }
}
