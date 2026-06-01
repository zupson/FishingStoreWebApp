package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CartProductDto;
import hr.algebra.fishingstore.dal.repos.CartProductRepository;
import hr.algebra.fishingstore.dal.repos.CartRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Cart;
import hr.algebra.fishingstore.model.entities.CartProduct;
import hr.algebra.fishingstore.model.entities.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public CartProductDto.ResponseDto getById(Long id) {
        CartProduct cartProduct = cartProductRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CART_PRODUCT_NOT_FOUND));
        return modelMapper.map(cartProduct, CartProductDto.ResponseDto.class);
    }

    public List<CartProductDto.ResponseDto> getAll() {
        return cartProductRepository.findAll()
                .stream()
                .map(cp -> modelMapper.map(cp, CartProductDto.ResponseDto.class))
                .toList();
    }

    public CartProductDto.ResponseDto create(CartProductDto.CreateDto dto) {
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new EntityNotFoundException(CART_NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        CartProduct cartProduct = modelMapper.map(dto, CartProduct.class);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        return modelMapper.map(cartProductRepository.save(cartProduct), CartProductDto.ResponseDto.class);
    }

    public CartProductDto.ResponseDto update(Long id, CartProductDto.EditDto dto) {
        CartProduct cartProduct = cartProductRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CART_PRODUCT_NOT_FOUND));

        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new EntityNotFoundException(CART_NOT_FOUND));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        modelMapper.map(dto, cartProduct);

        cartProduct.setCart(cart);
        cartProduct.setProduct(product);

        return modelMapper.map(cartProductRepository.save(cartProduct), CartProductDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        if (!cartProductRepository.existsById(id))
            return false;

        cartProductRepository.deleteById(id);
        return true;
    }
}