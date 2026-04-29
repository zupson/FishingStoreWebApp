package hr.algebra.fishingstore.dal.dtos;

import hr.algebra.fishingstore.model.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {
    public record CreateDto(
            @NotNull BigDecimal totalPrice,
            @NotNull OrderStatus orderStatus,
            @NotNull Long addressId,
            @NotNull Long userId
    ){}
    public record EditDto(
            BigDecimal totalPrice,
            OrderStatus orderStatus,
            Long addressId,
            Long userId
    ){}
    public record ResponseDto(
        Long id,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        Long addressId,
        Long userId
    ){}
}