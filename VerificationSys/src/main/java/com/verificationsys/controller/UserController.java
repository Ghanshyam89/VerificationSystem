package com.verificationsys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verificationsys.dto.UserResponse;
import com.verificationsys.request.UserRequest;
import com.verificationsys.services.UserService;
import com.verificationsys.util.ValidationUtils;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	public UserController(UserService userServiceMock) {
		this.userService = userServiceMock;
	}

	@PostMapping
	public ResponseEntity<?> createUsers(@RequestBody UserRequest userRequest) throws Exception {
		return ResponseEntity.ok(userService.createUsers(userRequest.getSize()));
	}

	@GetMapping
	public ResponseEntity<?> getUsers(@RequestParam(required = true) String sortType,
			@RequestParam(required = true) String sortOrder, 
			@RequestParam(defaultValue = "5") String limit,
			@RequestParam(defaultValue = "0") String offset) throws Exception {
		
		if (!isParsableToInt(limit) ) {
			throw new IllegalArgumentException("Invalid parameter values. Limit must be between 1 and 5");
		}

		if (!isParsableToInt(offset)) {
			throw new IllegalArgumentException("Invalid parameter values. Offset must be non-negative integer");
		}
		
		int parsedLimit = Integer.parseInt(limit);
        int parsedOffset = Integer.parseInt(offset);
        
		UserResponse users = userService.getUsers(sortType, sortOrder, parsedLimit, parsedOffset);
		return ResponseEntity.ok(users);
	}
	

	boolean isParsableToInt(String value) {
		return ValidationUtils.isParsableToInt(value);
    }
}
