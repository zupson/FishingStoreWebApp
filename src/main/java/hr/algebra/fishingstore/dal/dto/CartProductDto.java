package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class CartProductDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class CartProductBaseDto{
        @NotNull(message = "Quantity can not be empty")
        @Min(1)
        Integer quantity;
        @NotNull(message = "Cart id can not be empty")
        @Positive
        Long cartId;
        @NotNull(message = "Product id can not be empty")
        @Positive
        Long productId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CreateDto extends CartProductBaseDto{}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class EditDto extends  CartProductBaseDto{}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        Long id;
        Integer quantity;
        Long cartId;
        Long productId;
    }
}