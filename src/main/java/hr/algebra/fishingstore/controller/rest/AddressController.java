package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.AddressDto;
import hr.algebra.fishingstore.dal.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")

public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(addressService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<AddressDto.ResponseDto> create(@Valid @RequestBody AddressDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressDto.ResponseDto> update(@PathVariable Long id, @Valid @RequestBody AddressDto.EditDto editDto) {
        return ResponseEntity.ok(addressService.update(id, editDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = addressService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}