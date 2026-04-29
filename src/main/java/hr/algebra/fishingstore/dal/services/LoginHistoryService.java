package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.LoginHistoryDto;
import hr.algebra.fishingstore.dal.repos.LoginHistoryRepository;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.LoginHistory;
import hr.algebra.fishingstore.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository, UserRepository userRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<LoginHistoryDto.ResponseDto> getAll() {
        return loginHistoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public LoginHistoryDto.ResponseDto getById(Long id) {
        LoginHistory loginHistory = loginHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Login history not found."));

        return mapToResponseDto(loginHistory);
    }

    public LoginHistoryDto.ResponseDto create(LoginHistoryDto.CreateDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setIpAddress(dto.ipAddress());
        loginHistory.setSuccess(dto.success());
        loginHistory.setUser(user);

        LoginHistory createdLoginHistory = loginHistoryRepository.save(loginHistory);

        return mapToResponseDto(createdLoginHistory);
    }

    public LoginHistoryDto.ResponseDto update(Long id, LoginHistoryDto.UpdateDto dto) {
        LoginHistory loginHistory = loginHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Login history not found."));

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        loginHistory.setIpAddress(dto.ipAddress());
        loginHistory.setSuccess(dto.success());
        loginHistory.setUser(user);
        LoginHistory updatedLoginHistory = loginHistoryRepository.save(loginHistory);
        return mapToResponseDto(updatedLoginHistory);
    }


    public boolean delete(Long id) {
        if (!loginHistoryRepository.existsById(id)) return false;

        loginHistoryRepository.deleteById(id);
        return true;
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