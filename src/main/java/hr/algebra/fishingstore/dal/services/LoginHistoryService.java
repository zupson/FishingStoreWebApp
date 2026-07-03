package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.LoginHistoryDto;
import hr.algebra.fishingstore.dal.repos.LoginHistoryRepository;
import hr.algebra.fishingstore.dal.repos.UserRepository;
import hr.algebra.fishingstore.model.entities.LoginHistory;
import hr.algebra.fishingstore.model.entities.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {
    private static final String LOGIN_HISTORY_NOT_FOUND = "Login history not found.";
    private static final String USER_NOT_FOUND = "User not found.";

    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public LoginHistoryDto.ResponseDto getById(Long id) {
        LoginHistory loginHistory = loginHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(LOGIN_HISTORY_NOT_FOUND));

        LoginHistoryDto.ResponseDto dto = modelMapper.map(loginHistory, LoginHistoryDto.ResponseDto.class);
        dto.setUsername(loginHistory.getUser().getUsername());
        return dto;
    }

    public List<LoginHistoryDto.ResponseDto> getAll() {
        return loginHistoryRepository.findAllByOrderByLoginAtDesc()
                .stream()
                .map(lh -> {
                    LoginHistoryDto.ResponseDto dto = modelMapper.map(lh, LoginHistoryDto.ResponseDto.class);
                    dto.setUsername(lh.getUser().getUsername());
                    return dto;
                })
                .toList();
    }


    @Async
    public void create(String ipAddress, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setSuccess(true);
        loginHistory.setUser(user);

        loginHistoryRepository.save(loginHistory);
    }
}