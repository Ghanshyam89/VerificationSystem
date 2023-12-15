package com.verificationsys.error;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
	    String errorJson = createErrorJson(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorJson);
	}
	
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorJson = createErrorJson("Invalid request body format, Size should be between 1 and 5", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorJson);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception ex) {
        String errorJson = createErrorJson(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorJson);
    }

    private String createErrorJson(String message, int code) {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode errorJson = objectMapper.createObjectNode();
        errorJson.put("message", message);
        errorJson.put("code", code);
        errorJson.put("timestamp", new SimpleDateFormat("dd 'th' MMM yyyy HH:mm:ss").format(new Date()));

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ""; // Return empty string if JSON creation fails
        }
    }
}
