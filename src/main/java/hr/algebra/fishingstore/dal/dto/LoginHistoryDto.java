package hr.algebra.fishingstore.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class LoginHistoryDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        Long id;
        String ipAddress;
        boolean success;
        LocalDateTime loginAt;
        Long userId;
    }
}