package com.fuxuras.patisoru.exceptions;

import com.fuxuras.patisoru.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles cases where a user tries to register with an email that already exists.
     * The UserService should throw this exception in that scenario.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseMessage> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ResponseMessage message = new ResponseMessage(ex.getMessage(), -10); // Example error code
        return new ResponseEntity<>(message, HttpStatus.CONFLICT); // 409 Conflict
    }

    /**
     * Handles cases where email verification fails due to an invalid or expired token.
     * The UserService should throw this exception.
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseMessage> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        ResponseMessage message = new ResponseMessage(ex.getMessage(), -20); // Example error code
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    /**
     * A general handler for other unexpected exceptions to prevent exposing stack traces.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleGlobalException(Exception ex, WebRequest request) {
        ResponseMessage message = new ResponseMessage("An unexpected internal server error occurred.", -99);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}