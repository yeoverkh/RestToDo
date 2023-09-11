package ua.yehor.rest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.yehor.rest.todo.controller.UserController;
import ua.yehor.rest.todo.model.Role;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserEntityControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity("login1", "pass1"));
        userEntities.add(new UserEntity("login2", "pass1"));

        when(userService.getAllUsers()).thenReturn(userEntities);

        List<UserEntity> resultUserEntities = userController.getAllUsers();

        assertEquals(userEntities, resultUserEntities);
    }

    @Test
    void testDeleteUserById_Admin() throws AccessDeniedException {
        UserEntity adminUserEntity = new UserEntity("admin", "pass");
        adminUserEntity.setRoles(Set.of(Role.ADMIN));

        when(userService.getCurrentUser()).thenReturn(adminUserEntity);

        ResponseEntity<Void> responseEntity = userController.deleteUserByLogin("user");

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService, times(1)).deleteByLogin("user");
    }
}