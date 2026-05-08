package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<ProductDto.ResponseDto> create(@Valid @RequestBody ProductDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductDto.EditDto editDto) {
        return ResponseEntity.ok(productService.update(id, editDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = productService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}