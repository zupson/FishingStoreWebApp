package hr.algebra.fishingstore.dal.dtos;

import hr.algebra.fishingstore.model.enums.Currency;
import hr.algebra.fishingstore.model.enums.PaymentMethod;
import hr.algebra.fishingstore.model.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDto {

    public record CreateDto(
           @NotNull BigDecimal amount,
           @NotNull Currency currency,
           @NotNull PaymentMethod paymentMethod,
           @NotNull PaymentStatus paymentStatus,
           Long paypalId,
           @NotNull Long orderId
    ){}
    public record EditDto(
            BigDecimal amount,
            Currency currency,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            Long paypalId,
            Long orderId
    ){}
    public record ResponseDto(
            Long id,
            BigDecimal amount,
            Currency currency,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            LocalDateTime paidAt,
            LocalDateTime createdAt,
            Long paypalId,
            Long orderId
    ){}
}
