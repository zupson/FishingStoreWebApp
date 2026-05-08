package hr.algebra.fishingstore.dal.dto;

import hr.algebra.fishingstore.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        @NotNull(message = "Address id can not be empty")
        @Positive
        Long addressId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Order status can not be empty")
        OrderStatus orderStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{
        Long id;
        BigDecimal totalPrice;
        OrderStatus orderStatus;
        LocalDateTime createdAt;
        Long addressId;
        Long userId;
    }
}