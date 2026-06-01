package hr.algebra.fishingstore.dal.dto;

import hr.algebra.fishingstore.model.enums.Currency;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
        @NotNull(message = "Currency can not be empty")
        private Currency currency;
        @NotNull(message = "Payment method can not be empty")
        private PaymentMethod paymentMethod;
        @NotNull(message = "Order id can not be empty")
        @Positive
        private Long orderId;
        @Size(max = 100)
        private String paypalTransactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Payment status can not be empty")
        private PaymentStatus paymentStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private BigDecimal amount;
        private Currency currency;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;
        private Long orderId;
        private String paypalTransactionId;
    }
}