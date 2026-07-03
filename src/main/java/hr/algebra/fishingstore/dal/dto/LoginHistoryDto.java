package hr.algebra.fishingstore.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LoginHistoryDto {
    private LoginHistoryDto(){}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String ipAddress;
        private boolean success;
        private LocalDateTime loginAt;
        private Long userId;
        private String username;
    }
}