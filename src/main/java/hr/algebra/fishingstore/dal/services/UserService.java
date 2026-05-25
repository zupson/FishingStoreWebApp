package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.User;
import hr.algebra.fishingstore.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USER_NOT_FOUND = "User not found.";
    public static final String WRONG_CREDENTIALS = "Wrong password or username.";
    public static final String PASSWORDS_DOES_NOT_MATCH = "Passwords doesn't match.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public List<UserDto.ResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public UserDto.ResponseDto getById(Long id) {
        return mapToResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND)));
    }

    public UserDto.ResponseDto update(Long id, UserDto.EditDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());

        return mapToResponseDto(userRepository.save(user));
    }

    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) return false;

        userRepository.deleteById(id);
        return true;
    }

    public UserDto.AuthResponseDto login(UserDto.LoginDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new RuntimeException(WRONG_CREDENTIALS);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.create(user, refreshToken);

        return new UserDto.AuthResponseDto(accessToken, refreshToken, mapToResponseDto(user));

    }

    public UserDto.AuthResponseDto register(UserDto.RegisterDto dto) {
        User user = new User();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.create(savedUser, refreshToken);

        return new UserDto.AuthResponseDto(accessToken, refreshToken, mapToResponseDto(savedUser));
    }

    public boolean changePassword(UserDto.ChangePasswordDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));


        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new RuntimeException(PASSWORDS_DOES_NOT_MATCH);

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    private UserDto.ResponseDto mapToResponseDto(User user) {
        return new UserDto.ResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}