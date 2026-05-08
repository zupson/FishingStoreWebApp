package hr.algebra.fishingstore.dal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CategoryDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public abstract static class CategoryBaseDto {
        @NotBlank(message = "Name can not be empty.")
        @Size(min = 3, max = 150)
        String name;

        @NotBlank(message = "Description can not be empty")
        @Size(min = 10, max = 200)
        String description;
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
        Long id;
        String name;
        String description;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }
}