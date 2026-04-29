package hr.algebra.fishingstore.dal.dtos;


import jakarta.validation.constraints.NotNull;

public class CartProductDto {
    public record CreateDto(
            @NotNull Integer quantity,
            @NotNull Long cartId,
            @NotNull Long productId
    ){}
    public record EditDto(
            Integer quantity,
            Long cartId,
            Long productId
    ){}
    public record ResponseDto(
            Long id,
            Integer quantity,
            Long cartId,
            Long productId
    ){}
}