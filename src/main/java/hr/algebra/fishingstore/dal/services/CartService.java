package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CartDto;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartDto.ResponseDto getByUserId(Long userId) {
        return mapToResponseDto(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId)));
    }

    private CartDto.ResponseDto mapToResponseDto(Cart cart) {
        return new CartDto.ResponseDto(
                cart.getId(),
                cart.getUpdatedAt(),
                cart.getUser().getId()
        );
    }
}