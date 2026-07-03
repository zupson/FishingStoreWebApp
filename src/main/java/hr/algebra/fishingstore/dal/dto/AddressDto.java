package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class AddressDto {
    private AddressDto() {}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class AddressBaseDto{
        @NotBlank(message = "Street can not be empty")
        @Size(min=5,  max=100)
        private String street;
        @NotBlank(message = "City can not be empty")
        @Size(min=3,  max=100)
        private String city;
        @NotNull(message = "Postal code can not be empty")
        @Min(10000)
        @Max(9999999)
        private Integer postalCode;
        @NotBlank(message = "Country can not be empty")
        @Size(min=2,  max=100)
        private String country;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CreateDto extends AddressBaseDto{}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class EditDto extends AddressBaseDto {}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String street;
        private String city;
        private Integer postalCode;
        private String country;
    }
}