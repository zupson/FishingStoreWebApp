package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.CartProductDto;
import hr.algebra.fishingstore.dal.security.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.CartProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-products")
@RequiredArgsConstructor
public class CartProductController {
    private final CartProductService cartProductService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<CartProductDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(cartProductService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<CartProductDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cartProductService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<CartProductDto.ResponseDto> create(@Valid @RequestBody CartProductDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartProductService.create(createDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<CartProductDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody CartProductDto.EditDto editDto) {
        return ResponseEntity.ok(cartProductService.update(id, editDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = cartProductService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}