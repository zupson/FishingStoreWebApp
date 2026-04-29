package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.CartProductDto;
import hr.algebra.fishingstore.dal.repos.CartProductRepository;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import hr.algebra.fishingstore.model.entities.CartProduct;
import hr.algebra.fishingstore.model.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartProductService {
    private final CartProductRepository cartProductRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartProductService(CartProductRepository cartProductRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.cartProductRepository = cartProductRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartProductDto.ResponseDto getById(Long id) {
        CartProduct product = cartProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    public List<CartProductDto.ResponseDto> getAll() {
        return cartProductRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CartProductDto.ResponseDto create(CartProductDto.CreateDto dto) {
        Cart cart = cartRepository.findById(dto.cartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setQuantity(dto.quantity());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        CartProduct createdCartProduct = cartProductRepository.save(cartProduct);
        return mapToResponse(createdCartProduct);
    }

    public CartProductDto.ResponseDto update(Long id, CartProductDto.EditDto dto) {
        CartProduct cartProduct = cartProductRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart-Product not found"));

        Cart cart = cartRepository.findById(dto.cartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartProduct.setQuantity(dto.quantity());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        CartProduct updatedCartProduct = cartProductRepository.save(cartProduct);
        return mapToResponse(updatedCartProduct);
    }

    public boolean delete(Long id) {
        if(!cartProductRepository.existsById(id)) return false;

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