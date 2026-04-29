package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.UserDto;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.User;
import hr.algebra.fishingstore.model.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto.ResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public UserDto.ResponseDto getById(Long id) {
        // findById() vraća Optional<User> — kutija koja može biti prazna ili sadržavati User
        // orElseThrow() otvara tu kutiju:
        //    - ako ima User unutra  → izvadi ga i spremi u varijablu "user"
        //    - ako je kutija prazna → baci RuntimeException
        //ako maknemo .orElseThrow onda je tip Optional<User>, a sad je konkretan User
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return mapToResponseDto(user);
    }

    public UserDto.ResponseDto update(Long id, UserDto.UpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());

        User updatedUser = userRepository.save(user);
        return mapToResponseDto(updatedUser);
    }

    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) return false;

        userRepository.deleteById(id);
        return true;
    }

    public UserDto.ResponseDto login(UserDto.LoginDto dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword()))
            throw new RuntimeException("Wrong password.");

        return mapToResponseDto(user);
    }

    public UserDto.ResponseDto register(UserDto.RegisterDto dto) {
        User user = new User();

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        User registeredUser = userRepository.save(user);

        return mapToResponseDto(registeredUser);
    }

    public boolean changePassword(UserDto.ChangePasswordDto dto) {
        User user = userRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword()))
            throw new RuntimeException("Old password doesn't match.");

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
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