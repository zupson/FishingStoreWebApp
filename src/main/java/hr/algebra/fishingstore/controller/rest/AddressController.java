package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.AddressDto;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<AddressDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(addressService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<AddressDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<AddressDto.ResponseDto> create(@Valid @RequestBody AddressDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(createDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<AddressDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody AddressDto.EditDto editDto) {
        return ResponseEntity.ok(addressService.update(id, editDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = addressService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}