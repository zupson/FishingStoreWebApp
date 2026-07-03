package hr.algebra.fishingstore.dal.services.init;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.dal.services.AuthService;
import hr.algebra.fishingstore.exceptions.DuplicateUserException;
import hr.algebra.fishingstore.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInit implements CommandLineRunner {
    public static final String ADMIN_CREATED = "Admin has been registered successfully";
    public static final String ADMIN_EXIST = "Admin already exist";
    public static final String ERROR_MESSAGE_ADMIN = "Error while registering admin";

    private final UserRepository userRepository;
    private final AuthService authService;

    @Value("${app.admin.first-name}")
    private String firstName;
    @Value("${app.admin.last-name}")
    private String lastName;
    @Value("${app.admin.email}")
    private String email;
    @Value("${app.admin.username}")
    private String username;
    @Value("${app.admin.password}")
    private String password;

    @Override
    public void run(String... args) {
        try {
            if (!userRepository.existsByRole(Role.ADMIN)) {
                authService.register(
                        new UserDto.RegisterDto(
                                firstName,
                                lastName,
                                email,
                                username,
                                password), Role.ADMIN
                );

                log.info(ADMIN_CREATED);
            } else {
                log.info(ADMIN_EXIST);
            }
        } catch (DuplicateUserException | IllegalArgumentException e) {
            throw new RuntimeException(ERROR_MESSAGE_ADMIN, e);
        }
    }
}
