package hr.algebra.fishingstore.dal.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CategoryDto {

    public record CreateDto(
            @NotBlank String name,
            @NotBlank String description,
            @NotBlank String image
    ) {}

    public record EditDto(
            @NotBlank String name,
            @NotBlank String description,
            @NotBlank String image
    ) {}

    public record ResponseDto(
            Long id,
            String name,
            String description,
            String image,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
