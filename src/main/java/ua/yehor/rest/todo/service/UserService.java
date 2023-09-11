package ua.yehor.rest.todo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        UserEntity currentUserEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity foundUserEntity = userRepository.findByLogin(currentUserEntity.getLogin()).orElse(null);

        if (foundUserEntity == null) {
            throw new UsernameNotFoundException("Cannot find user with this login");
        }

        return foundUserEntity;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteByLogin(String login) throws AccessDeniedException {
        UserEntity userEntity = getCurrentUser();

        if (userEntity.isNotAdmin()) {
            throw new AccessDeniedException("Unauthorized to remove user");
        }

        userRepository.deleteByLogin(login);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(EntityNotFoundException::new);
    }
}