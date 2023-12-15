package com.verificationsys.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.verificationsys.dto.PageInfo;
import com.verificationsys.dto.UserInfo;
import com.verificationsys.dto.UserResponse;
import com.verificationsys.entities.User;
import com.verificationsys.request.UserRequest;
import com.verificationsys.services.UserService;

public class UserControllerTestNew {

	@Mock
	private UserService userServiceMock;

	@InjectMocks
	private UserController userController;
	private UserResponse result;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		userServiceMock = mock(UserService.class);
		userController = new UserController(userServiceMock);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

		// Create a list of User objects
		List<UserInfo> userInfoList = Arrays.asList(
				new UserInfo("Phoebe Mortensen", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")),
						"Male", 31, "NL", "TO_BE_VERIFIED"),
				new UserInfo("Tammy Porter", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")),
						"Male", 51, "NL", "VERIFIED"),
				new UserInfo("Camila Pereira", dateFormat.format(dateFormat.parse("1974-08-18T00:55:36.097+00:00")),
						"Male", 41, "NL", "TO_BE_VERIFIED"));

		// Create a PageInfo object
		PageInfo pageInfo = new PageInfo();
		pageInfo.setHasNextPage(true);
		pageInfo.setHasPreviousPage(false);
		pageInfo.setTotal(3L);

		// Create a Result object that includes the list of users and the PageInfo
		result = new UserResponse(userInfoList, pageInfo);
	}

	@Test
	public void testCreateUsers() throws Exception {
		// Arrange
		UserRequest userRequest = new UserRequest();
		userRequest.setSize(3);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//        com.verificationsys.entities.User.User(String name, int age, String gender, Date dob, String nationality, String verificationStatus, Date dateCreated, Date dateModified)
		List<User> userList = Arrays.asList(
				new User("Tammy Porter", 50, "Male", dateFormat.parse("1974-08-18T00:55:36.097+00:00"), "AU",
						"VERIFIED", new Date(), new Date()),
				new User("Camila Pereira", 41, "Female", dateFormat.parse("1974-08-18T00:55:36.097+00:00"), "IN",
						"NON-VERIFIED", new Date(), new Date()),
				new User("Phoebe Mortensen", 31, "Male", dateFormat.parse("1974-08-18T00:55:36.097+00:00"), "NZ",
						"VERIFIED", new Date(), new Date()));

		when(userServiceMock.createUsers(anyInt())).thenReturn(userList); // Replace with your desired result

		// Act
		ResponseEntity<?> responseEntity = userController.createUsers(userRequest);

		// Assert
		assertEquals(200, responseEntity.getStatusCodeValue()); // Assuming OK status
		// Add more assertions based on your requirements
	}

	@Test
    public void testGetUsers() throws Exception {
        // Arrange
        when(userServiceMock.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(result); // Replace with your desired result

        // Act
        ResponseEntity<?> responseEntity = userController.getUsers("Phoebe Mortensen", "even", "5", "0");

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue()); // Assuming OK status
        // Add more assertions based on your requirements
    }

	@Test
	public void testGetUsers_ValidLimitAndOffset() throws Exception {
		// Act
		ResponseEntity<?> responseEntity = userController.getUsers("Tammy Porter", "even", "5", "0");

		// Assert
		assertEquals(200, responseEntity.getStatusCodeValue()); // Assuming OK status
		// Add more assertions based on your requirements
		verify(userServiceMock).getUsers(anyString(), anyString(), anyInt(), anyInt());
	}

//	@Test
//	void shouldGetUsersbySorting() throws Exception {
//
//		PageInfo pageInfo = new PageInfo();
//		pageInfo.setHasNextPage(true);
//		pageInfo.setHasPreviousPage(false);
//		pageInfo.setTotal(3L);
//		
//		// Mock UserService
//        UserService userServiceMock = mock(UserService.class);
//
//        // Create UserController with the mocked UserService
//        UserController userController = new UserController(userServiceMock);
//		
//		// Set up your mock to return the Result object when the method you're testing
//		// is called
//    	when(userServiceMock.getUsers(anyString(), anyString(), anyInt(), anyInt())).thenReturn(result);
//
//    	// Perform the request and assert the response
//    	this.mockMvc
//    		.perform(get("/users").param("sortType", "age").param("sortOrder", "even").param("limit", "-2")
//    		.param("offset", "0"))
//    		.andExpect(status().isOk()).andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
//    		.andExpect(jsonPath("$.data[0].name").value("Tammy Porter"))
//    		.andExpect(jsonPath("$.pageInfo.total").value(pageInfo.getTotal()))
//    		.andExpect(jsonPath("$.pageInfo.hasNextPage").value(pageInfo.isHasNextPage()))
//    		.andExpect(jsonPath("$.pageInfo.hasPreviousPage").value(pageInfo.isHasPreviousPage()));
//    }


}
