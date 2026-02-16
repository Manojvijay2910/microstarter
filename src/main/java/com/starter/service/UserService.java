package com.starter.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.starter.dto.user.UserRequestDto;
import com.starter.dto.user.UserResponseDto;
import com.starter.json.ResponseJson;

public interface UserService {

	UserResponseDto createUser(UserRequestDto request);
	
	List<UserResponseDto> getActiveUsers();
	
    UserResponseDto getUserById(Long id);
    
    UserResponseDto getUserByEmail(String email);
	
	UserResponseDto updateUser(UserRequestDto request);
    
    UserResponseDto updateUserStatusById(Long id);

    ResponseJson<UserResponseDto> getUsers(Pageable pageable, String searchBoxVal);
    
}
