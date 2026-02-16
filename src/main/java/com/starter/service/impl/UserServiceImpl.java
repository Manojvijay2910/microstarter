package com.starter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.starter.constants.StarterConstants;
import com.starter.convertor.EntityToResponseConvertor;
import com.starter.convertor.RequestToEntityConvertor;
import com.starter.dto.user.UserRequestDto;
import com.starter.dto.user.UserResponseDto;
import com.starter.entity.User;
import com.starter.exception.UserNotFoundException;
import com.starter.json.ResponseJson;
import com.starter.repository.UserRepository;
import com.starter.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository userRepository;
	
    private final PasswordEncoder passwordEncoder;
    
    private final RequestToEntityConvertor requestToEntityConvertor;
    
    private final EntityToResponseConvertor entityToResponseConvertor;
    
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RequestToEntityConvertor requestToEntityConvertor,
            EntityToResponseConvertor entityToResponseConvertor) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.requestToEntityConvertor = requestToEntityConvertor;
        this.entityToResponseConvertor = entityToResponseConvertor;
    }

	@Override
	public UserResponseDto createUser(UserRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEnabled(StarterConstants.ENABLED);

	    user = requestToEntityConvertor.convertUser(request, user);

	    if (request.getPassword() != null && !request.getPassword().isBlank()) {
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	    }

	    User savedUser = userRepository.save(user);

	    return entityToResponseConvertor.convertUser(savedUser);
	}
	
	@Override
	public ResponseJson<UserResponseDto> getUsers(Pageable pageable, String searchParam) {
		ResponseJson<UserResponseDto> userJson = new ResponseJson<UserResponseDto>();
		
		Page<User> userList = null;
		
		if(!StringUtils.isEmpty(searchParam)) {
			userList = userRepository.findByNameContainingAndEnabled(pageable, searchParam, StarterConstants.ENABLED);
		} else {
			userList = userRepository.findByEnabled(pageable, StarterConstants.ENABLED);
		}
		
		if(userList != null){
			List<User> entityList = userList.getContent();
			List<UserResponseDto> beanList = new ArrayList<UserResponseDto>();
			if(entityList!= null && !entityList.isEmpty()){
				int index = pageable.getPageNumber()*pageable.getPageSize();
				for(User entity : entityList){
					UserResponseDto bean = entityToResponseConvertor.convertUser(entity);
					bean.setIndex(++index);
					beanList.add(bean);
				}
			}
			userJson.setiTotalDisplayRecords(userList.getNumberOfElements());
			userJson.setiTotalRecords(userList.getTotalElements());
			userJson.setAaData(beanList);
		}
		return userJson;
	}
	
	@Override
	public List<UserResponseDto> getActiveUsers() {
		List<User> userList = userRepository.findByEnabled(StarterConstants.ENABLED);
		
		List<UserResponseDto> dtoList = new ArrayList<>();
		
		for(User entity: userList) {
			UserResponseDto dto = entityToResponseConvertor.convertUser(entity);
			dtoList.add(dto);
		}
		return dtoList;
	}

	@Override
	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

	    return entityToResponseConvertor.convertUser(user);
	}

	@Override
	public UserResponseDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
		
		return entityToResponseConvertor.convertUser(user);
	}
	
	@Override
	public UserResponseDto updateUser(UserRequestDto request) {
		
		User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getId()));

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user = requestToEntityConvertor.convertUser(request, user);

	    if (request.getPassword() != null && !request.getPassword().isBlank()) {
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	    }

	    User savedUser = userRepository.save(user);

	    return entityToResponseConvertor.convertUser(savedUser);
	}
	
	@Override
	public UserResponseDto updateUserStatusById(Long id) {
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
		
		user.setEnabled(user.getEnabled() == StarterConstants.ENABLED ? StarterConstants.DISABLED : StarterConstants.ENABLED);
		
		User saved = userRepository.save(user);
		
		return entityToResponseConvertor.convertUser(saved);
	}

}
