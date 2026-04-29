package hr.algebra.fishingstore.dal.dtos;

import hr.algebra.fishingstore.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class UserDto {

    public record RegisterDto(
            @NotBlank String firstName,
            @NotBlank String lastName,
            @NotBlank @Email String email,
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record LoginDto(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record ChangePasswordDto(
            Long id,
            @NotBlank String oldPassword,
            @NotBlank String newPassword
    ) {}

    public record UpdateDto(
            String firstName,
            String lastName,
            String email,
            String username
    ) {}

    public record ResponseDto(
            Long id,
            String firstName,
            String lastName,
            String email,
            String username,
            Role role,
            boolean enabled,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}