package ua.yehor.rest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.yehor.rest.todo.config.AdminInitializer;
import ua.yehor.rest.todo.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminInitializer adminInitializer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        adminInitializer.setAdminLogin("admin");
        adminInitializer.setAdminPassword("admin_password");

        when(passwordEncoder.encode("admin_password")).thenReturn("encoded_password");
    }

    @Test
    void testRun_AdminUserInitialization() {
        adminInitializer.run();

        verify(userRepository).save(any());
    }
}