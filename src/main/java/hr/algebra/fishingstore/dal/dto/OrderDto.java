package hr.algebra.fishingstore.dal.dto;

import hr.algebra.fishingstore.model.enums.OrderStatus;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
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
        private Long addressId;
        @NotNull(message = "Payment method can not be empty")
        private PaymentMethod paymentMethod;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Order status can not be empty")
        private OrderStatus orderStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{
        private Long id;
        private BigDecimal totalPrice;
        private OrderStatus orderStatus;
        private LocalDateTime createdAt;
        private Long addressId;
        private Long userId;
        private String approvalUrl;
    }
}