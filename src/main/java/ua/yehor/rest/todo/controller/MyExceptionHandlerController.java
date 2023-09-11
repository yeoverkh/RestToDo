package ua.yehor.rest.todo.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Locale;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class MyExceptionHandlerController implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    private MessageSourceAccessor getMessageSourceAccessor() {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return new MessageSourceAccessor(messageSource, currentLocale);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = getMessageSourceAccessor().getMessage("error.unauthorized");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseException() {
        String errorMessage = getMessageSourceAccessor().getMessage("error.unprocessable_json");
        return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleCreatingAlreadyExistsEntity() {
        String errorMessage = getMessageSourceAccessor().getMessage("error.entity_exists");
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUpdatingNotFoundEntity() {
        String errorMessage = getMessageSourceAccessor().getMessage("error.entity_not_found");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleTryingToChangeFinalState() {
        String errorMessage = getMessageSourceAccessor().getMessage("error.cannot_change_status");
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidTask(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed.";
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }
}