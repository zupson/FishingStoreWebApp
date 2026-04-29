package hr.algebra.fishingstore.dal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LoginHistoryDto {

    public record CreateDto(
            @NotBlank String ipAddress,
            boolean success,
            @NotNull Long userId
    ){}
    public record UpdateDto(
            String ipAddress,
            boolean success,
            Long userId
    ){}
    public record ResponseDto(
            Long id,
            String ipAddress,
            boolean success,
            LocalDateTime loginAt,
            Long userId
    ){}
}