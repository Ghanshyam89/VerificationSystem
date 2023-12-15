package com.verificationsys.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verificationsys.dto.PageInfo;
import com.verificationsys.dto.UserInfo;
import com.verificationsys.dto.UserResponse;
import com.verificationsys.entities.User;
import com.verificationsys.request.UserRequest;
import com.verificationsys.services.UserService;

@WebMvcTest
public class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateSizeUsers() throws Exception {
		int size = 2;
		List<User> list = new ArrayList<User>();

		list.add(new User());
		list.add(new User());
//		list.add(new User());

		when(userService.createUsers(size)).thenReturn(list);

		UserRequest userRequest = new UserRequest();
		userRequest.setSize(2);

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(size));
	}

	@Test
    public void testGetUsers() throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		// Create a list of User objects
		List<UserInfo> userInfoList = Arrays.asList(
				new UserInfo("Phoebe Mortensen", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 31, "NL",
						"TO_BE_VERIFIED"),
				new UserInfo("Tammy Porter", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 51, "NL",
						"VERIFIED"),
				new UserInfo("Camila Pereira", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 41, "NL",
						"TO_BE_VERIFIED"));

		// Create a PageInfo object
		PageInfo pageInfo = new PageInfo();
		pageInfo.setHasNextPage(true);
		pageInfo.setHasPreviousPage(false);
		pageInfo.setTotal(3L);

		// Create a Result object that includes the list of users and the PageInfo
		UserResponse result = new UserResponse(userInfoList, pageInfo);
		
        // Mock UserService
        UserService userServiceMock = mock(UserService.class);

        // Create UserController with the mocked UserService
        UserController userController = new UserController(userServiceMock);
//        userController.setUserService(userServiceMock);

        when(userServiceMock.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(result);
        // Perform the API call
        ResponseEntity<?> responseEntity = userController.getUsers("age", "odd", "5", "0");

        // Validate the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // Add additional assertions based on your expected response content
    }
	
	@Test
	void shouldGetUsersbySorting() throws Exception {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		// Create a list of User objects
		List<UserInfo> userInfoList = Arrays.asList(
				new UserInfo("Phoebe Mortensen", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 31, "NL",
						"TO_BE_VERIFIED"),
				new UserInfo("Tammy Porter", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 51, "NL",
						"VERIFIED"),
				new UserInfo("Camila Pereira", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")), "Male", 41, "NL",
						"TO_BE_VERIFIED"));

		// Create a PageInfo object
		PageInfo pageInfo = new PageInfo();
		pageInfo.setHasNextPage(true);
		pageInfo.setHasPreviousPage(false);
		pageInfo.setTotal(3L);

		// Create a Result object that includes the list of users and the PageInfo
		UserResponse result = new UserResponse(userInfoList, pageInfo);

		// Set up your mock to return the Result object when the method you're testing
		// is called
		when(userService.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(result);

		// Perform the request and assert the response
		this.mockMvc
				.perform(get("/users").param("sortType", "age").param("sortOrder", "even").param("limit", "-2")
						.param("offset", "0"))
				.andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
				.andExpect(jsonPath("$.data[0].name").value("Phoebe Mortensen"))
				.andExpect(jsonPath("$.pageInfo.total").value(pageInfo.getTotal()))
				.andExpect(jsonPath("$.pageInfo.hasNextPage").value(pageInfo.isHasNextPage()))
				.andExpect(jsonPath("$.pageInfo.hasPreviousPage").value(pageInfo.isHasPreviousPage()));

	}

//	@Test
//	void shouldGetUsersbySortingbyOddName() throws Exception {
//
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//
//		// Create a list of User objects
//		List<UserInfo> userInfoList = Arrays.asList(
//				new UserInfo("Tammy Porter", dateFormat.format("1974-08-18T00:55:36.097+00:00"), "Male", 51, "NL",
//						"TO_BE_VERIFIED"),
//				new UserInfo("Phoebe Mortensen", dateFormat.format("1974-08-18T00:55:36.097+00:00"), "Male", 31, "AU",
//						"TO_BE_VERIFIED"),
//				new UserInfo("Camila Pereira", dateFormat.format("1974-08-18T00:55:36.097+00:00"), "Male", 41, "NL",
//						"TO_BE_VERIFIED"));
//
//		// Create a PageInfo object
//		PageInfo pageInfo = new PageInfo();
//		pageInfo.setHasNextPage(true);
//		pageInfo.setHasPreviousPage(false);
//		pageInfo.setTotal(3L);
//
//		// Create a Result object that includes the list of users and the PageInfo
//		UserResponse result = new UserResponse(userInfoList, pageInfo);
//
//		// Set up your mock to return the Result object when the method you're testing
//		// is called
//		when(userService.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(result);
//
//		// Perform the request and assert the response
//		this.mockMvc
//				.perform(get("/users").param("sortType", "Name").param("sortOrder", "odd").param("limit", "2")
//						.param("offset", "0"))
//				.andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
//				.andExpect(jsonPath("$.data[0].name").value("Tammy Porter"))
//				.andExpect(jsonPath("$.pageInfo.total").value(pageInfo.getTotal()))
//				.andExpect(jsonPath("$.pageInfo.hasNextPage").value(pageInfo.isHasNextPage()))
//				.andExpect(jsonPath("$.pageInfo.hasPreviousPage").value(pageInfo.isHasPreviousPage()));
//
//	}

}
