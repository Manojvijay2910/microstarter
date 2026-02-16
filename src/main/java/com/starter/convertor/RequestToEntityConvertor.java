package com.starter.convertor;

import org.springframework.stereotype.Component;

import com.starter.dto.user.UserRequestDto;
import com.starter.entity.User;

@Component
public class RequestToEntityConvertor {

	public User convertUser(UserRequestDto request, User user) {
		if(request.getId() != null) {
			user.setId(request.getId());
		}
		user.setName(request.getName());
	    user.setEmail(request.getEmail());
	    return user;
	}
}
