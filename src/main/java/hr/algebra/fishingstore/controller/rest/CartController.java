package hr.algebra.fishingstore.controller.rest;

import hr.algebra.fishingstore.dal.dtos.CartDto;
import hr.algebra.fishingstore.dal.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")

public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto.ResponseDto> getByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getByUserId(id));
    }
}