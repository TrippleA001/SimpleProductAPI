package com.myjavainternship.productapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError; // Needed to get specific field validation errors
import org.springframework.web.bind.MethodArgumentNotValidException; // The exception thrown when @Valid fails
import org.springframework.web.bind.annotation.ControllerAdvice; // Sticker: Makes this class apply to ALL controllers
import org.springframework.web.bind.annotation.ExceptionHandler; // Sticker: Marks a method as handling a specific exception type
import org.springframework.web.context.request.WebRequest; // Gives context about the incoming request
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler; // Base class with helpful methods

import java.time.LocalDateTime; // To add a timestamp to errors
import java.util.LinkedHashMap; // A Map that remembers the order items were added
import java.util.Map; // General type for key-value pairs
import java.util.stream.Collectors; // Used for processing the validation errors

// @ControllerAdvice: A sticker telling Spring: "This class contains advice (like exception handling)
// that should apply across multiple controllers globally."
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler { // Extend base class for common web exceptions

  // --- Handler for our custom "Resource Not Found" error ---
  // @ExceptionHandler(ResourceNotFoundException.class): Sticker telling Spring:
  // "If a ResourceNotFoundException is thrown anywhere, run THIS method."
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(
          ResourceNotFoundException ex, WebRequest request) {

    // Create a Map to hold the details of our custom error response
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now()); // When the error occurred
    body.put("status", HttpStatus.NOT_FOUND.value()); // The HTTP status code (404)
    body.put("error", "Not Found"); // A short error description
    body.put("message", ex.getMessage()); // The specific message from the exception we threw
    body.put("path", request.getDescription(false).replace("uri=", "")); // The URL path requested

    // Return the error map as the response body with a 404 NOT_FOUND status
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  // --- Handler for Validation Errors (when @Valid fails) ---
  // We OVERRIDE a method from the base class to customize the response for validation errors.
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex, // The exception containing validation details
          HttpHeaders headers,
          HttpStatusCode status, // The status code (usually 400 Bad Request)
          WebRequest request) {

    // Create the error response map
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value()); // Should be 400
    body.put("error", "Validation Error");
    body.put("path", request.getDescription(false).replace("uri=", ""));

    // Extract the specific field errors from the exception
    // Example: "name": "Product name is required...", "price": "Price must be greater than 0..."
    Map<String, String> fieldErrors = ex.getBindingResult()
            .getFieldErrors() // Get list of errors
            .stream() // Process the list
            .collect(Collectors.toMap( // Collect into a new Map
                    FieldError::getField, // Key is the field name (e.g., "name")
                    FieldError::getDefaultMessage // Value is the error message defined in @NotBlank, @Min etc.
            ));

    body.put("errors", fieldErrors); // Add the map of field errors to our response body

    // Return the custom error response with the original status (400)
    return new ResponseEntity<>(body, headers, status);
  }

  // --- Fallback Handler for any other unexpected errors ---
  @ExceptionHandler(Exception.class) // Catches any other Exception that wasn't caught above
  public ResponseEntity<Object> handleGenericException(
          Exception ex, WebRequest request) {

    // Log the unexpected error so developers can investigate (very important!)
    // The base class 'ResponseEntityExceptionHandler' has a logger we can use.
    logger.error("An unexpected error occurred: ", ex);

    // Create a generic error response for the user
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 status code
    body.put("error", "Internal Server Error");
    body.put("message", "An unexpected error occurred. Please try again later."); // Don't expose internal details
    body.put("path", request.getDescription(false).replace("uri=", ""));

    // Return the generic error response with a 500 INTERNAL_SERVER_ERROR status
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}