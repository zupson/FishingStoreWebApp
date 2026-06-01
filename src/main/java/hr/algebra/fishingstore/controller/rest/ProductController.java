package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.services.ProductService;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ProductController.BASE_URL)
@RequiredArgsConstructor
public class ProductController {
    static final String BASE_URL = PathConst.API + PathConst.PRODUCTS;
    private static final String PRODUCT = "product";
    private static final String IMAGE = "image";
    private final ProductService productService;

    @GetMapping
    @PermitAll
    public ResponseEntity<List<ProductDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PermitAll
    public ResponseEntity<ProductDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<ProductDto.ResponseDto> create(@RequestPart(PRODUCT) @Valid ProductDto.CreateDto createDto,
                                                         @RequestPart(value = IMAGE, required = true) MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(createDto, image));
    }

    @PutMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<ProductDto.ResponseDto> update(@PathVariable Long id,
                                                         @RequestPart(PRODUCT) @Valid ProductDto.EditDto editDto,
                                                         @RequestPart(value = IMAGE, required = false) MultipartFile image) {
        return ResponseEntity.ok(productService.update(id, editDto, image));
    }

    @DeleteMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = productService.delete(id);
        if (!deleted)
            return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}