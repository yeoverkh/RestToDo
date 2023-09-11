package ua.yehor.rest.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{login}")
    public UserEntity getUser(@PathVariable String login) {
        return userService.getByLogin(login);
    }

    @DeleteMapping("/{login}")
    public ResponseEntity<Void> deleteUserByLogin(@PathVariable String login) throws AccessDeniedException {
        userService.deleteByLogin(login);

        return ResponseEntity.noContent().build();
    }
}
