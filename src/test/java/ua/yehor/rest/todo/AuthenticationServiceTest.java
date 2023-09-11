package ua.yehor.rest.todo;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.yehor.rest.todo.service.JwtService;
import ua.yehor.rest.todo.dto.AuthenticationRequestDTO;
import ua.yehor.rest.todo.dto.AuthenticationResponseDTO;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.UserRepository;
import ua.yehor.rest.todo.service.AuthenticationService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testuser", "testpassword");

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("testToken");

        AuthenticationResponseDTO response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegister_UserAlreadyExists() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("existinguser", "testpassword");

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(EntityExistsException.class, () -> authenticationService.register(request));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testuser", "testpassword");

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(new UserEntity()));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("testToken");

        AuthenticationResponseDTO response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("nonexistinguser", "testpassword");

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}