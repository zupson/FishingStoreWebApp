package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class ProductOrderDto {
    private ProductOrderDto(){}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        @NotNull(message = "Quantity can not be empty")
        @Positive
        private Integer quantity;
        @NotNull(message = "Order id can not be empty")
        @Positive
        private Long orderId;
        @NotNull(message = "Product id can not be empty")
        @Positive
        private Long productId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Quantity can not be empty")
        @Positive
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private Integer quantity;
        private BigDecimal priceAtPurchase;
        private Long orderId;
        private Long productId;
        private ProductDto.ResponseDto product;
    }
}