package hr.algebra.fishingstore.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CartDto {
    private CartDto(){}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private LocalDateTime updatedAt;
        private Long userId;
    }
}