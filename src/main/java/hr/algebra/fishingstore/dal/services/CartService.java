package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CartDto;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    public static final String CART_NOT_FOUND_FOR_USER = "Cart not found for user: ";

    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public CartDto.ResponseDto getByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(CART_NOT_FOUND_FOR_USER + userId));

        return  modelMapper.map(cart, CartDto.ResponseDto.class);
    }
}