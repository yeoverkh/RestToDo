package ua.yehor.rest.todo;


import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ua.yehor.rest.todo.controller.MyExceptionHandlerController;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MyExceptionHandlerControllerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MyExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testHandleJsonParseException() {
        when(messageSource.getMessage("error.unprocessable_json", null, Locale.getDefault()))
                .thenReturn("Unprocessable JSON");

        ResponseEntity<String> responseEntity = exceptionHandlerController.handleJsonParseException();

        assertEquals("Unprocessable JSON", responseEntity.getBody());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    void testHandleCreatingAlreadyExistsEntity() {
        when(messageSource.getMessage("error.entity_exists", null, Locale.getDefault()))
                .thenReturn("Entity already exists");

        ResponseEntity<String> responseEntity = exceptionHandlerController.handleCreatingAlreadyExistsEntity();

        assertEquals("Entity already exists", responseEntity.getBody());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testCommence() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);

        exceptionHandlerController.commence(request, response, exception);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, null);
    }

    @Test
    void testHandleUpdatingNotFoundEntity() {
        when(messageSource.getMessage("error.entity_not_found", null, Locale.getDefault()))
                .thenReturn("Entity not found");

        ResponseEntity<String> responseEntity = exceptionHandlerController.handleUpdatingNotFoundEntity();

        assertEquals("Entity not found", responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testHandleTryingToChangeFinalState() {
        when(messageSource.getMessage("error.cannot_change_status", null, Locale.getDefault()))
                .thenReturn("Cannot change status");

        ResponseEntity<String> responseEntity = exceptionHandlerController.handleTryingToChangeFinalState();

        assertEquals("Cannot change status", responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testHandleInvalidTask() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(fieldError.getDefaultMessage()).thenReturn("Validation failed.");

        ResponseEntity<String> responseEntity = exceptionHandlerController.handleInvalidTask(exception);

        assertEquals("Validation failed.", responseEntity.getBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }
}