package com.crm.global;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestControllerAdvice
public class GlobalExceptionHandler {

	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Map<String, String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
	    Map<String, String> errors = new HashMap<>();
	    String str="Duplicate entry";
	    if (ex.getMessage().contains(str)) {
	        // Extract the duplicate entry value (e.g., "welcome 10")
	        String duplicateEntry = ex.getMessage().substring(ex.getMessage().indexOf(str) + 16, ex.getMessage().indexOf("for key") - 2);
	        errors.put("error", str);
	        errors.put("message", "A campaign with the name '" + duplicateEntry + "' already exists. Please use a different name.");
	    } else {
	        errors.put("error", "Data integrity violation");
	        errors.put("message", "An unexpected data integrity error occurred.");
	    }
	    return errors;
	}
	@ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.badRequest().body("Invalid Date : " + ex.getParsedString());
    }
}