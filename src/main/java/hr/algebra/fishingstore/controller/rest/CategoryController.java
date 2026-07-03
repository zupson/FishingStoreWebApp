package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.CategoryService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CategoryController.BASE_URL)
@RequiredArgsConstructor
public class CategoryController {
    static final String BASE_URL = PathConst.API + PathConst.CATEGORIES;

    private final CategoryService categoryService;

    @GetMapping
    @PermitAll
    public ResponseEntity<List<CategoryDto.ResponseDto>> getAll() {
        return ResponseEntity
                .ok(categoryService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PermitAll
    public ResponseEntity<CategoryDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity
                .ok(categoryService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<CategoryDto.ResponseDto> create(@Valid @RequestBody CategoryDto.CreateDto createDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(createDto));
    }

    @PutMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<CategoryDto.ResponseDto> update(@PathVariable Long id,
                                                          @Valid @RequestBody CategoryDto.EditDto editDto) {
        return ResponseEntity
                .ok(categoryService.update(id, editDto));
    }

    @DeleteMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = categoryService.delete(id);
        if (!deleted)
            return ResponseEntity
                    .notFound()
                    .build();

        return ResponseEntity
                .noContent()
                .build();
    }
}