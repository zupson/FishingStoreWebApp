package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ProductOrderDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        @NotNull(message = "Quantity can not be empty")
        @Positive
        Integer quantity;
        @NotNull(message = "Order id can not be empty")
        @Positive
        Long orderId;
        @NotNull(message = "Product id can not be empty")
        @Positive
        Long productId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Quantity can not be empty")
        @Positive
        Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        Long id;
        Integer quantity;
        BigDecimal priceAtPurchase;
        Long orderId;
        Long productId;
    }
}