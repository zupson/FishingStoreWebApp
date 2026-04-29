package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.CategoryDto;
import hr.algebra.fishingstore.dal.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto.ResponseDto> create(@Valid @RequestBody CategoryDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody CategoryDto.EditDto editDto) {
        return ResponseEntity.ok(categoryService.update(id, editDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = categoryService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}