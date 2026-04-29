package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.ProductOrderDto;
import hr.algebra.fishingstore.dal.services.ProductOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-orders")

public class ProductOrderController {
    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @GetMapping
    public ResponseEntity<List<ProductOrderDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(productOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productOrderService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductOrderDto.ResponseDto> create(@Valid @RequestBody ProductOrderDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productOrderService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductOrderDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductOrderDto.EditDto editDto) {
        return ResponseEntity.ok(productOrderService.update(id, editDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = productOrderService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}