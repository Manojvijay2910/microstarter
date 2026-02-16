package com.starter.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.thymeleaf.util.StringUtils;

import com.starter.dto.ApiResponse;
import com.starter.dto.user.UserRequestDto;
import com.starter.dto.user.UserResponseDto;
import com.starter.json.ResponseJson;
import com.starter.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    private static final String SEARCH_VAL = "searchBoxVal";
	
	private static final String SORT_COL = "iSortCol_0";
	
	private static final String SORT_DIR = "sSortDir_0";
	
	private static final String COL_NAME = "mDataProp_";
	
	private static final String DISPLAY_LENGTH = "iDisplayLength";
	
	private static final String DISPLAY_START = "iDisplayStart";
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody UserRequestDto userRequest) {

        UserResponseDto user = userService.createUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User created", user));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ResponseJson<UserResponseDto>>> getAllUsers(
    		HttpServletRequest request) {
    	String searchBoxVal = request.getParameter(SEARCH_VAL);
		String sSortCol = request.getParameter(SORT_COL);
		String sSortDir = request.getParameter(SORT_DIR);
		String sColName = request.getParameter(COL_NAME + sSortCol);
		Integer pageNumber = 0;
		Integer pageDisplayLength = request.getParameter(DISPLAY_LENGTH) != null ? 
				Integer.valueOf(request.getParameter(DISPLAY_LENGTH)) : 10;
		
		if (null != request.getParameter(DISPLAY_START)) {
			pageNumber = (Integer.valueOf(request.getParameter(DISPLAY_START)) / pageDisplayLength);
		}
		Sort sort;
		if (sColName != null) {
			if (StringUtils.equals("asc", sSortDir)) {
				sort = Sort.by(Direction.ASC, sColName);;
			} else {
				sort = Sort.by(Direction.DESC, sColName);
			}
		} else {
			sort = Sort.by(Direction.DESC, "id");// default sorting
		}
		Pageable pageable = PageRequest.of(pageNumber, pageDisplayLength, sort);
		
		ResponseJson<UserResponseDto> cJson = userService.getUsers(pageable, searchBoxVal);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Users retrieved", cJson));
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getActiveUsers() {
    	List<UserResponseDto> users = userService.getActiveUsers();
    	
    	return ResponseEntity.ok(
    			new ApiResponse<>(true, "Users retrieved", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "User retrieved",
                        userService.getUserById(id)));
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable Long id,
    		@Valid @RequestBody UserRequestDto userRequest) {
    	
    	userRequest.setId(id);
    	UserResponseDto user = userService.updateUser(userRequest);
    	
    	return ResponseEntity.ok(new ApiResponse<>(true, "User updated", user));
    }
    
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateStatus(@PathVariable Long id) {
    	UserResponseDto user = userService.updateUserStatusById(id);
    	
    	return ResponseEntity.ok(new ApiResponse<>(true, "User status updated", user));
    }
}
