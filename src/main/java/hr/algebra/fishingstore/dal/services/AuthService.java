package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.dal.services.token.JwtService;
import hr.algebra.fishingstore.dal.services.token.RefreshTokenService;
import hr.algebra.fishingstore.model.entities.User;
import hr.algebra.fishingstore.model.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public static final String WRONG_CREDENTIALS = "Wrong password or username.";
    public static final String PASSWORDS_DOES_NOT_MATCH = "Passwords doesn't match.";
    public static final String USER_NOT_FOUND = "User not found.";

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;

    public UserDto.AuthResponseDto register(UserDto.RegisterDto dto, Role role) {
        User user = initUser(dto, role);

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        refreshTokenService.create(savedUser, refreshToken);

        return new UserDto.AuthResponseDto(
                accessToken,
                refreshToken,
                modelMapper.map(savedUser, UserDto.ResponseDto.class));
    }

    private User initUser(UserDto.RegisterDto dto, Role role) {
        User user = modelMapper.map(dto, User.class);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setEnabled(true);
        return user;
    }


    public UserDto.AuthResponseDto login(UserDto.LoginDto dto) {

        User user = getUserByUsername(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new BadCredentialsException(WRONG_CREDENTIALS);

        String accessToken = jwtService.generateAccessToken(user);

        refreshTokenService.deleteByUser(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenService.create(user, refreshToken);

        return new UserDto.AuthResponseDto(
                accessToken,
                refreshToken,
                modelMapper.map(user, UserDto.ResponseDto.class));

    }

    public void logout(String refreshToken) {
        refreshTokenService.delete(refreshToken);
    }

    public boolean changePassword(UserDto.ChangePasswordDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new BadCredentialsException(PASSWORDS_DOES_NOT_MATCH);

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }
}