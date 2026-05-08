package hr.algebra.fishingstore.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CartDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        Long id;
        LocalDateTime updatedAt;
        Long userId;
    }
}