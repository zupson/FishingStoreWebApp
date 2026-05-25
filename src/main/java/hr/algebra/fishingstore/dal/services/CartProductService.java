package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CartProductDto;
import hr.algebra.fishingstore.dal.repos.CartProductRepository;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import hr.algebra.fishingstore.model.entities.CartProduct;
import hr.algebra.fishingstore.model.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartProductService {
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CART_PRODUCT_NOT_FOUND = "Cart-Product not found";

    private final CartProductRepository cartProductRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartProductDto.ResponseDto getById(Long id) {
        return mapToResponse(cartProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND)));
    }

    public List<CartProductDto.ResponseDto> getAll() {
        return cartProductRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CartProductDto.ResponseDto create(CartProductDto.CreateDto dto) {
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new RuntimeException(CART_NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(dto.getQuantity());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        return mapToResponse(cartProductRepository.save(cartProduct));
    }

    public CartProductDto.ResponseDto update(Long id, CartProductDto.EditDto dto) {
        CartProduct cartProduct = cartProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(CART_PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new RuntimeException(CART_NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

        cartProduct.setQuantity(dto.getQuantity());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        return mapToResponse(cartProductRepository.save(cartProduct));
    }

    public boolean delete(Long id) {
        if (!cartProductRepository.existsById(id)) return false;

        cartProductRepository.deleteById(id);
        return true;
    }

    private CartProductDto.ResponseDto mapToResponse(CartProduct product) {
        return new CartProductDto.ResponseDto(
                product.getId(),
                product.getQuantity(),
                product.getCart().getId(),
                product.getProduct().getId()
        );
    }
}