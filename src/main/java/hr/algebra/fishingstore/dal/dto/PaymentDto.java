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
        Currency currency;
        @NotNull(message = "Payment method can not be empty")
        PaymentMethod paymentMethod;
        @NotNull(message = "Order id can not be empty")
        @Positive
        Long orderId;
        @Size(max = 100)
        String paypalTransactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotNull(message = "Payment status can not be empty")
        PaymentStatus paymentStatus;
        @Size(max = 100)
        String paypalTransactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        Long id;
        BigDecimal amount;
        Currency currency;
        PaymentMethod paymentMethod;
        PaymentStatus paymentStatus;
        LocalDateTime paidAt;
        LocalDateTime createdAt;
        Long orderId;
        String paypalTransactionId;
    }
}