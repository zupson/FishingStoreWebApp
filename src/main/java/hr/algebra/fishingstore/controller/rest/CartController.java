package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dto.CartDto;
import hr.algebra.fishingstore.dal.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto.ResponseDto> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getByUserId(id));
    }
}