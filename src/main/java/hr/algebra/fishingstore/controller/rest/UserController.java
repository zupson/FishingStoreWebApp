package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<UserDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<UserDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/register")
    @PermitAll
    public ResponseEntity<UserDto.AuthResponseDto> register(@Valid @RequestBody UserDto.RegisterDto registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerDto));
    }

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<UserDto.AuthResponseDto> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<UserDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserDto.EditDto updateDto) {
        return ResponseEntity.ok(userService.update(id, updateDto));
    }

    @PatchMapping("/change-password")
    @PreAuthorize(RoleBasedAccessConst.USER_ONLY)
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody UserDto.ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(userService.changePassword(changePasswordDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}