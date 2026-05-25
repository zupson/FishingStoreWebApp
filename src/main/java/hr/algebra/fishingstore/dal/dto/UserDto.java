package hr.algebra.fishingstore.dal.dto;

import hr.algebra.fishingstore.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDto {
        @NotBlank(message = "First name can not be empty")
        @Size(min = 3, max = 100)
        private String firstName;

        @NotBlank(message = "Last name can not be empty")
        @Size(min = 3, max = 100)
        private String lastName;

        @NotBlank(message = "Email name can not be empty")
        @Email
        @Size(min = 5, max = 100)
        private String email;

        @NotBlank(message = "Username can not be empty")
        @Size(min = 3, max = 50)
        private String username;

        @NotBlank(message = "Password can not be empty")
        @Size(min = 8, max = 50)
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        @NotBlank(message = "Username can not be empty")
        @Size(min = 3, max = 50)
        private String username;
        @NotBlank(message = "Password can not be empty")
        @Size(min = 8, max = 50)
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePasswordDto {
        @NotBlank(message = "Old password can not be empty")
        @Size(min = 8, max = 50)
        private String oldPassword;
        @NotBlank(message = "New password can not be empty")
        @Size(min = 8, max = 50)
        private String newPassword;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditDto {
        @NotBlank(message = "First name can not be empty")
        @Size(min = 3, max = 100)
        private String firstName;

        @NotBlank(message = "Last name can not be empty")
        @Size(min = 3, max = 100)
        private String lastName;

        @NotBlank(message = "Email name can not be empty")
        @Email
        @Size(min = 5, max = 100)
        private String email;

        @NotBlank(message = "Username can not be empty")
        @Size(min = 3, max = 50)
        private String username;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private Role role;
        private boolean enabled;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponseDto {
        private String accessToken;
        private String refreshToken;
        private UserDto.ResponseDto user;
    }
}