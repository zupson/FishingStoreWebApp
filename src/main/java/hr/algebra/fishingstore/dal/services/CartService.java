package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.CartDto;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartDto.ResponseDto getByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        return mapToResponseDto(cart);
    }

    private CartDto.ResponseDto mapToResponseDto(Cart cart) {
        return new CartDto.ResponseDto(
                cart.getId(),
                cart.getUpdatedAt(),
                cart.getUser().getId()
        );
    }
}