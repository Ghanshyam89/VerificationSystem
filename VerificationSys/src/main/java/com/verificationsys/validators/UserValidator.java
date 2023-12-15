package com.verificationsys.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

@Service
public class UserValidator {

	@Autowired
	@Qualifier("nationalizeWebClient")
	private WebClient nationalizeWebClient;
	@Autowired
	@Qualifier("genderizeWebClient")
	private WebClient genderizeWebClient;

	@Value("${nationalize.api.url}")
	private String fetchNationalityApiUrl;

	@Value("${gender.api.url}")
	private String fetchGenderApiUrl;	
	
	public CompletableFuture<Boolean> validateNationality(String name, String expectedNationality) {
		return nationalizeWebClient.get().uri(fetchNationalityApiUrl+"?name=" + name).retrieve().toEntity(String.class)
				.map(responseEntity -> {
					try {
						String responseBody = responseEntity.getBody();
						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode rootNode = objectMapper.readTree(responseBody);
						JsonNode countryArray = rootNode.get("country");

						for (JsonNode country : countryArray) {
							String countryId = country.get("country_id").asText();
							if (countryId.equals(expectedNationality)) {
								return true;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
				}).toFuture();
	}

	public CompletableFuture<Boolean> validateGender(String name, String expectedGender) {
		return genderizeWebClient.get().uri(fetchGenderApiUrl+"?name=" + name).retrieve().toEntity(String.class)
				.map(responseEntity -> {
					try {
						String responseBody = responseEntity.getBody();
						ObjectMapper objectMapper = new ObjectMapper();
						JsonNode rootNode = objectMapper.readTree(responseBody);
						String gender = rootNode.get("gender").asText();

						return gender.equals(expectedGender);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}).toFuture();
	}
	
	public String validateUser(String name, String nationality, String gender, int age) {
	    CompletableFuture<Boolean> nationalityValidation = CompletableFuture.supplyAsync(() -> validateNationality(name, nationality))
	            .thenCompose(result -> result); // Flatten the nested CompletableFuture

	    CompletableFuture<Boolean> genderValidation = CompletableFuture.supplyAsync(() -> validateGender(name, gender))
	            .thenCompose(result -> result); // Flatten the nested CompletableFuture

	    CompletableFuture<Void> combinedValidation = CompletableFuture.allOf(nationalityValidation, genderValidation);

	    combinedValidation.join(); // Waits for both validations to complete

	    // Extracting the results
	    boolean nationalityResult = nationalityValidation.join();
	    boolean genderResult = genderValidation.join();

	    if (nationalityResult && genderResult) {
	        return "VERIFIED";
	    } else {
	        return "TO_BE_VERIFIED";
	    }
	}


	


}
