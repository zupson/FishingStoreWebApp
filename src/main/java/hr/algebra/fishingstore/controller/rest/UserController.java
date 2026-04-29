package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.UserDto;
import hr.algebra.fishingstore.dal.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto.ResponseDto> register(@Valid @RequestBody UserDto.RegisterDto registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.ResponseDto> login(@Valid @RequestBody UserDto.LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserDto.UpdateDto updateDto) {
        return ResponseEntity.ok(userService.update(id, updateDto));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@Valid @RequestBody UserDto.ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok(userService.changePassword(changePasswordDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}