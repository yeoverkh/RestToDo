package ua.yehor.rest.todo;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import ua.yehor.rest.todo.service.JwtService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "E079817FC13404FB23D83B52DC92AD9AC4806B432DF2B3BDF95C6FF8E5B32375";
    private static final int EXPIRATION_TIME = 3600000;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expirationTimeInMilliseconds", EXPIRATION_TIME);
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);

        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    void testExtractClaim() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("testUser", username);
    }

    @Test
    void testExtractExpiration() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);

        Date expirationDate = jwtService.extractExpiration(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }
}