package hr.algebra.fishingstore.dal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddressDto {
    public record CreateDto(
            @NotBlank String street,
            @NotBlank String city,
            @NotNull Integer postalCode,
            @NotBlank String country
    ) {}

    public record EditDto(
            @NotBlank String street,
            @NotBlank String city,
            @NotNull Integer postalCode,
            @NotBlank String country
    ) {}

    public record ResponseDto(
            Long id,
            String street,
            String city,
            Integer postalCode,
            String country
    ) {}
}
