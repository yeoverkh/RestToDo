package ua.yehor.rest.todo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.yehor.rest.todo.model.Role;
import ua.yehor.rest.todo.model.UserEntity;
import ua.yehor.rest.todo.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        UserEntity adminUserEntity = new UserEntity();
        adminUserEntity.setLogin(adminLogin);
        adminUserEntity.setPassword(passwordEncoder.encode(adminPassword));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        roles.add(Role.ADMIN);
        adminUserEntity.setRoles(roles);

        userRepository.save(adminUserEntity);
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}