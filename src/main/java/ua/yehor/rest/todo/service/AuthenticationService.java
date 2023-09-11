package ua.yehor.rest.todo.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.yehor.rest.todo.dto.AuthenticationRequestDTO;
import ua.yehor.rest.todo.dto.AuthenticationResponseDTO;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(AuthenticationRequestDTO request) {
        if (isUserPresentByLogin(request.getLogin())) {
            throw new EntityExistsException("Entity with this login is already exists");
        }

        UserEntity userEntity = new UserEntity(
                request.getLogin(),
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(userEntity);

        String jwtToken = jwtService.generateToken(userEntity);

        return new AuthenticationResponseDTO(jwtToken);
    }

    private boolean isUserPresentByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));

        UserEntity userEntity = userRepository.findByLogin(request.getLogin()).orElseThrow();

        String jwtToken = jwtService.generateToken(userEntity);

        return new AuthenticationResponseDTO(jwtToken);
    }
}
