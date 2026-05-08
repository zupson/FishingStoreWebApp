package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.PaymentDto;
import hr.algebra.fishingstore.dal.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @PostMapping()
    public ResponseEntity<PaymentDto.ResponseDto> create(@Valid @RequestBody PaymentDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(createDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody PaymentDto.EditDto editDto) {
        return ResponseEntity.ok(paymentService.update(id, editDto));
    }
}