package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.OrderDto;
import hr.algebra.fishingstore.dal.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto.ResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.ResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDto.ResponseDto> create(@Valid @RequestBody OrderDto.CreateDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(createDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto.ResponseDto> update(@PathVariable Long id,@Valid @RequestBody OrderDto.EditDto editDto) {
        return ResponseEntity.ok(orderService.update(id,editDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = orderService.delete(id);
        if(!deleted) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();
    }
}