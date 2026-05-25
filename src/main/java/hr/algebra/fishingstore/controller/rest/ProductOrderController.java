package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.ProductOrderDto;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.ProductOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-orders")
@RequiredArgsConstructor
public class ProductOrderController {
    private final ProductOrderService productOrderService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<ProductOrderDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(productOrderService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<ProductOrderDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productOrderService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<ProductOrderDto.ResponseDto> create(@Valid @RequestBody ProductOrderDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productOrderService.create(createDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<ProductOrderDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductOrderDto.EditDto editDto) {
        return ResponseEntity.ok(productOrderService.update(id, editDto));
    }
}