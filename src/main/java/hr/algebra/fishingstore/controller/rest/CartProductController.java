package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.CartProductDto;
import hr.algebra.fishingstore.dal.services.CartProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-products")

public class CartProductController {
    private final CartProductService cartProductService;

    public CartProductController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @GetMapping
    public ResponseEntity<List<CartProductDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(cartProductService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartProductDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cartProductService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CartProductDto.ResponseDto> create(@Valid @RequestBody CartProductDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartProductService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartProductDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody CartProductDto.EditDto editDto) {
        return ResponseEntity.ok(cartProductService.update(id, editDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = cartProductService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}