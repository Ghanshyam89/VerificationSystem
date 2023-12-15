package com.verificationsys.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.verificationsys.dto.PageInfo;
import com.verificationsys.dto.UserInfo;
import com.verificationsys.dto.UserResponse;
import com.verificationsys.entities.User;
import com.verificationsys.repository.UserRepository;
import com.verificationsys.sorting.SortByAge;
import com.verificationsys.sorting.SortByName;
import com.verificationsys.sorting.UserSortStrategy;
import com.verificationsys.validators.EnglishAlphabetsValidator;
import com.verificationsys.validators.NumericValidator;
import com.verificationsys.validators.UserValidator;
import com.verificationsys.validators.Validator;

import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserValidator userValidator;

	private UserSortStrategy sortStrategy;
	
	@Autowired
	private UserRepository userrepository;

	@Autowired
	@Qualifier("randomUserWebClient")
	private WebClient randomUserWebClient;
	
	@Value("${random-user.api.url}")
	private String fetchUserApiUrl;

	public List<User> createUsers(int size) throws Exception {
		List<User> userList = new ArrayList<>();

		if (size < 1 || size > 5) {
			throw new IllegalArgumentException("Size should be between 1 and 5");
		}

		for (int i = 0; i < size; i++) {
			// Use randomUserWebClient to call the RandomUser API
			Mono<String> randomUserResponse = randomUserWebClient.get().uri(fetchUserApiUrl).retrieve()
					.bodyToMono(String.class);

			String res = randomUserResponse.block(); // block to wait for the result

			// Parsing the JSON response
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode userNode = objectMapper.readTree(res).path("results").get(0);

			// Extracting name, age, gender, dob, and nationality
			String firstName = userNode.path("name").path("first").asText();
			String lastName = userNode.path("name").path("last").asText();
			int age = userNode.path("dob").path("age").asInt();
			String gender = userNode.path("gender").asText();

			// Parse ISO 8601 date format with milliseconds
			Date dob = parseDate(userNode.path("dob").path("date").asText());

			String nationality = userNode.path("nat").asText();

			// Validating First Name using EnglishAlphabetsValidator
			validateFieldforPost(firstName, EnglishAlphabetsValidator.getInstance(), "First Name");

			// Validating Last Name using EnglishAlphabetsValidator
			validateFieldforPost(lastName, EnglishAlphabetsValidator.getInstance(), "Last Name");

			// Validating age using NumericValidator
			validateFieldforPost(age, NumericValidator.getInstance(), "Age");

			// Verifying if the details match among the APIs.
			String verificationStatus = userValidator.validateUser(firstName, nationality, gender, age);

			// Get the current time
			Date currentTime = new Date();
			User user = new User(firstName + " " + lastName, age, gender, dob, nationality, verificationStatus,
					currentTime, currentTime);

			userList.add(user);
		}
		userrepository.saveAll(userList);
		return userList;
	}

	public UserResponse getUsers(String sortType, String sortOrder, int limit, int offset) throws Exception {
		// Validate input parameters
		validateInputParameters(sortType, sortOrder, limit, offset);

		// Retrieve data from the database
		List<User> userList = userrepository.findUsersWithLimitAndOffset(limit, offset);
		
		// Apply sorting based on sortType and sortOrder
		List<User> SortedUsers = sortUsers(userList, sortType, sortOrder);

		// Convert entities to DTOs
		List<UserInfo> userInfoList = SortedUsers.stream().map(this::convertToUserInfo)
				.collect(Collectors.toList());

		// getting minUserId and maxUSerId for pageInfo
		Long minUserId = userList.stream().min(Comparator.comparing(User::getUserId))
				.orElseThrow(() -> new RuntimeException("User not Found")).getUserId();

		Long maxUserId = userList.stream().max(Comparator.comparing(User::getUserId))
				.orElseThrow(() -> new RuntimeException("User not Found")).getUserId();

		// Create PageInfo
		PageInfo pageInfo = new PageInfo(
				(maxUserId < userrepository.findMaxUserId()) ? true : false,
				(minUserId > userrepository.findMinUserId()) ? true : false, 
				userrepository.count());

		return new UserResponse(userInfoList, pageInfo);
	}

	private void validateInputParameters(String sortType, String sortOrder, int limit, int offset) {
		validateFieldforGet(sortType, EnglishAlphabetsValidator.getInstance(), "sortType");
		validateFieldforGet(sortOrder, EnglishAlphabetsValidator.getInstance(), "sortOrder");

		validateFieldforGet(limit, NumericValidator.getInstance(), "limit");
		validateFieldforGet(offset, NumericValidator.getInstance(), "offset");

		if (limit < 1 || limit > 5) {
			throw new IllegalArgumentException("Limit must be between 1 and 5");
		}

	}

	private UserInfo convertToUserInfo(User user) {
		// Convert User to UserInfo DTO
		return new UserInfo(user.getName(), parseDatetoString(user.getDob()), user.getGender(), user.getAge(), user.getNationality(),
				user.getVerificationStatus());
	}

	private List<User> sortUsers(List<User> users, String sortType, String sortOrder) {

	    if (!("EVEN".equalsIgnoreCase(sortOrder) || "Odd".equalsIgnoreCase(sortOrder))) {
	        throw new IllegalArgumentException("The SortOrder must be EVEN or ODD");
	    }

	    if ("Age".equalsIgnoreCase(sortType)) {
	        sortStrategy = new SortByAge(sortOrder);
	    } else if ("Name".equalsIgnoreCase(sortType)) {
	        sortStrategy = new SortByName(sortOrder);
	    } else {
	        throw new IllegalArgumentException("Invalid sortType, SortType must be Name or Age");
	    }

	    sortStrategy.sort(users);

	    return users;
	}
	
	private <T> boolean validateFieldforPost(T value, Validator<T> validator, String fieldName) {
		if (!validator.validate(value)) {
			if(fieldName.equals("size")) {
				throw new IllegalArgumentException("Size should be between 1 and 5");
			}else {
				throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
			}
		}
		return true;
	}
	
	private <T> boolean validateFieldforGet(T value, Validator<T> validator, String fieldName) {
		if (!validator.validate(value)) {
			if (fieldName.equals("SortOrder")) {
				throw new IllegalArgumentException("The SortOrder must be EVEN or ODD");
			}else if(fieldName.equals("sortType")) {
				throw new IllegalArgumentException("Invalid sortType");
			}else if(fieldName.equals("limit")) {
				throw new IllegalArgumentException("Limit must be between 1 and 5");
			}else if(fieldName.equals("offset")) {
				throw new IllegalArgumentException("Offset must be non-negative integer");
			}
//			throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
//			return false;
		}
		return true;
	}
	
	private String parseDatetoString(Date date) {
		try {
			return new SimpleDateFormat("dd MMM yyyy").format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Date parseDate(String dateString) {
		try {
			if (!dateString.isEmpty()) {
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(dateString);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
