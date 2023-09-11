package ua.yehor.rest.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import ua.yehor.rest.todo.model.UserEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class UserEntityDetailsServiceTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void testLoadUserByUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin("user");
        userEntity.setPassword("password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);

        UserDetails loadedUserDetails = userDetailsService.loadUserByUsername("user");

        assertNotNull(loadedUserDetails);
        assertEquals("user", loadedUserDetails.getUsername());
        assertEquals("password", loadedUserDetails.getPassword());
    }
}