package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.services.AuthService;
import hr.algebra.fishingstore.model.enums.Role;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.BASE_URL)
@RequiredArgsConstructor
public class AuthController {
    static final String BASE_URL = PathConst.API + PathConst.AUTH;
    private final AuthService authService;

    @PostMapping(PathConst.REGISTER)
    @PermitAll
    public ResponseEntity<UserDto.AuthResponseDto> register(@Valid @RequestBody UserDto.RegisterDto registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerDto, Role.USER));
    }

    @PostMapping(PathConst.LOGIN)
    @PermitAll
    public ResponseEntity<UserDto.AuthResponseDto> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping(PathConst.LOGOUT)
    @PreAuthorize((RoleBasedAccessConst.AUTHENTICATED))
    public ResponseEntity<Void> logout(@Valid @RequestBody UserDto.LogoutDto logoutDto) {
        authService.logout(logoutDto.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(PathConst.CHANGE_PASSWORD)
    @PreAuthorize(RoleBasedAccessConst.USER_ONLY)
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody UserDto.ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(authService.changePassword(changePasswordDto));
    }
}
