package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.LoginHistoryDto;
import hr.algebra.fishingstore.dal.repos.LoginHistoryRepository;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.LoginHistory;
import hr.algebra.fishingstore.model.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;

    public List<LoginHistoryDto.ResponseDto> getAll() {
        return loginHistoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public LoginHistoryDto.ResponseDto getById(Long id) {
        return mapToResponseDto(loginHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Login history not found.")));
    }

    public LoginHistoryDto.ResponseDto create(HttpServletRequest request) {
        String currentLoggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(currentLoggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found."));

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setIpAddress(request.getRemoteAddr());
        loginHistory.setUser(user);

        return mapToResponseDto(loginHistoryRepository.save(loginHistory));
    }

    private LoginHistoryDto.ResponseDto mapToResponseDto(LoginHistory loginHistory) {
        return new LoginHistoryDto.ResponseDto(
                loginHistory.getId(),
                loginHistory.getIpAddress(),
                loginHistory.isSuccess(),
                loginHistory.getLoginAt(),
                loginHistory.getUser() != null ? loginHistory.getUser().getId() : null
        );
    }
}