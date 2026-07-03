package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {
    private ProductDto(){}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class ProductBaseDto {
        @NotBlank(message = "Name can not be empty")
        @Size(min = 3, max = 150)
        private String name;
        @NotBlank(message = "Description can not be empty")
        @Size(min = 10, max = 500)
        private String description;
        @NotNull(message = "Price can not be empty")
        @Positive
        private BigDecimal price;
        boolean onStock;
        @NotNull(message = "Category id can not be empty")
        @Positive
        private Long categoryId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CreateDto extends ProductBaseDto {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class EditDto extends ProductBaseDto{}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private boolean onStock;
        private String imagePath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long categoryId;
    }
}