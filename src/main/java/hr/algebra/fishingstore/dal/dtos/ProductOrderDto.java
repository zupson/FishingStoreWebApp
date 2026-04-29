package hr.algebra.fishingstore.dal.dtos;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductOrderDto {
    public record CreateDto(
           @NotNull Integer quantity,
           @NotNull BigDecimal priceAtPurchase,
           @NotNull Long orderId,
           @NotNull Long productId
    ){}
    public record EditDto(
            Integer quantity,
            BigDecimal priceAtPurchase,
            Long orderId,
            Long productId
    ){}
    public record ResponseDto(
            Long id,
            Integer quantity,
            BigDecimal priceAtPurchase,
            Long orderId,
            Long productId
    ){}
}