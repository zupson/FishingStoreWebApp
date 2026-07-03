package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CategoryDto {
    private CategoryDto(){}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class CategoryBaseDto {
        @NotBlank(message = "Name can not be empty.")
        @Size(min = 3, max = 150)
        private String name;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CreateDto extends CategoryBaseDto {
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class EditDto extends CategoryBaseDto {
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}