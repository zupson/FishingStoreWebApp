package hr.algebra.fishingstore.dal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {
    public record CreateDto(
            @NotBlank String name,
            @NotBlank String description,
            @NotNull BigDecimal price,
            boolean onStock,
            @NotBlank String image,
            @NotNull Long categoryId
    ) {
    }

    public record EditDto(
            String name,
            String description,
            BigDecimal price,
            boolean onStock,
            String image,
            Long categoryId
    ) {
    }

    public record ResponseDto(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean onStock,
            String image,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long categoryId
    ) {}
}