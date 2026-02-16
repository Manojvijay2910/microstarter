package com.starter.convertor;

import org.springframework.stereotype.Component;

import com.starter.dto.user.UserResponseDto;
import com.starter.entity.User;

@Component
public class EntityToResponseConvertor {
	
	public UserResponseDto convertUser(User user) {
		UserResponseDto response = new UserResponseDto();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setEnabled(user.getEnabled());
	    return response;
	}
}
