package ua.yehor.rest.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.yehor.rest.todo.model.Role;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.UserRepository;
import ua.yehor.rest.todo.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUser_UserExists() {
        UserEntity userEntity = new UserEntity("user", "pass");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByLogin("user")).thenReturn(Optional.of(userEntity));

        UserEntity foundUserEntity = userService.getCurrentUser();
        assertEquals(userEntity, foundUserEntity);

        verify(userRepository).findByLogin("user");
    }

    @Test
    void testGetCurrentUser_UserNotFound() {
        UserEntity userEntity = new UserEntity("user", "pass");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userEntity);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByLogin("user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getCurrentUser());

        verify(userRepository).findByLogin("user");
    }

    @Test
    void testGetAllUsers() {
        UserEntity userEntity1 = new UserEntity("user1", "pass");
        UserEntity userEntity2 = new UserEntity("user2", "pass");
        List<UserEntity> userEntityList = List.of(userEntity1, userEntity2);
        when(userRepository.findAll()).thenReturn(userEntityList);

        List<UserEntity> foundUserEntities = userService.getAllUsers();

        assertEquals(userEntityList, foundUserEntities);

        verify(userRepository).findAll();
    }

    @Test
    void testDeleteByLogin_UserExists() throws AccessDeniedException {
        UserEntity adminEntity = new UserEntity("admin", "pass");
        adminEntity.setRoles(Set.of(Role.ADMIN));

        when(userRepository.findByLogin(any())).thenReturn(Optional.of(adminEntity));

        userService.deleteByLogin("user");

        verify(userRepository).findByLogin("user");
    }

    @Test
    void testDeleteById_UserNotFound() {
        Optional<UserEntity> optionalUser = Optional.empty();
        when(userRepository.findByLogin("user")).thenReturn(optionalUser);

        assertThrows(UsernameNotFoundException.class, () -> userService.deleteByLogin("user"));

        verify(userRepository).findByLogin("user");
    }

    @Test
    void testSaveUser() {
        UserEntity userEntity = new UserEntity("user", "pass");

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = userService.save(userEntity);

        verify(userRepository).save(userEntity);
        assertEquals(userEntity, result);
    }
}