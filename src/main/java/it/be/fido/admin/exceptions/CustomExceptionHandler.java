package it.be.fido.admin.exceptions;

import it.be.fido.admin.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageResponse handleInvalidArgument(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.add(fieldError.getField() + " " + fieldError.getDefaultMessage())
        );
        String error = String.join(",", errors);
        int statusCode = exception.getStatusCode().value();
        return new MessageResponse(error, statusCode);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public MessageResponse handleRuntimeException(RuntimeException exception) {
        return new MessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoleNotExistsException.class)
    public MessageResponse handleRoleNotExistsException(RoleNotExistsException exception) {
        return new MessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    private MessageResponse handleMessageAuth(BadCredentialsException exception) {
        return new MessageResponse("Invalid username or password. Please retry.", HttpStatus.UNAUTHORIZED.value());
    }

}