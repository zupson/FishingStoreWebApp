package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.utilities.PathConst;
import hr.algebra.fishingstore.utilities.RoleBasedAccessConst;
import hr.algebra.fishingstore.dal.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PaymentController.BASE_URL)
@RequiredArgsConstructor
public class PaymentController {
    static final String BASE_URL = PathConst.API + PathConst.PAYMENTS;
    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<List<PaymentDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_OR_RESOURCE_OWNER)
    public ResponseEntity<PaymentDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @PostMapping()
    @PreAuthorize(RoleBasedAccessConst.USER_ONLY)
    public ResponseEntity<PaymentDto.ResponseDto> create(@Valid @RequestBody PaymentDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(createDto));
    }

    @PutMapping(PathConst.ID)
    @PreAuthorize(RoleBasedAccessConst.ADMIN_ONLY)
    public ResponseEntity<PaymentDto.ResponseDto> update(@PathVariable Long id,
                                                         @Valid @RequestBody PaymentDto.EditDto editDto) {
        return ResponseEntity.ok(paymentService.update(id, editDto));
    }
}