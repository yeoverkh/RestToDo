package ua.yehor.rest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.yehor.rest.todo.controller.AuthenticationController;
import ua.yehor.rest.todo.dto.AuthenticationRequestDTO;
import ua.yehor.rest.todo.dto.AuthenticationResponseDTO;
import ua.yehor.rest.todo.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testUsername", "testPassword");
        AuthenticationResponseDTO expectedResponse = new AuthenticationResponseDTO("testToken");

        when(authenticationService.register(request)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponseDTO> responseEntity = authenticationController.register(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testUsername", "testPassword");
        AuthenticationResponseDTO expectedResponse = new AuthenticationResponseDTO("testToken");

        when(authenticationService.authenticate(request)).thenReturn(expectedResponse);

        ResponseEntity<AuthenticationResponseDTO> responseEntity = authenticationController.authenticate(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}