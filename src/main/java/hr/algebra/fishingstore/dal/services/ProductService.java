package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Category;
import hr.algebra.fishingstore.model.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductDto.ResponseDto getById(Long id) {
        return mapToResponseDto(productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND)));
    }

    public List<ProductDto.ResponseDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ProductDto.ResponseDto create(ProductDto.CreateDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND));

        Product product = new Product();

        return setupProduct(product, category, dto.getName(), dto.getDescription(), dto.getPrice(), dto.isOnStock(), dto);
    }

    public ProductDto.ResponseDto update(Long id, ProductDto.EditDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND));

        return setupProduct(product, category, dto.getName(), dto.getDescription(), dto.getPrice(), dto.isOnStock(), dto);
    }

    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) return false;

        productRepository.deleteById(id);
        return true;
    }

    private ProductDto.ResponseDto setupProduct(Product product, Category category, String name, String description, BigDecimal price, boolean onStock, ProductDto.ProductBaseDto dto) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setOnStock(onStock);
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
    }

    private ProductDto.ResponseDto mapToResponseDto(Product product) {
        return new ProductDto.ResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isOnStock(),
                product.getImagePath(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategory().getId()
        );
    }
}