package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.UserDto;
import hr.algebra.fishingstore.dal.services.UserService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController {
    static final String BASE_URL = PathConst.API + PathConst.USERS;
    private final UserService userService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<UserDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<UserDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<UserDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserDto.EditDto updateDto) {
        return ResponseEntity.ok(userService.update(id, updateDto));
    }

    @DeleteMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (!deleted)
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}