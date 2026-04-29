package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.ProductDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.model.entities.Category;
import hr.algebra.fishingstore.model.entities.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductDto.ResponseDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapToResponseDto(product);
    }

    public List<ProductDto.ResponseDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ProductDto.ResponseDto create(ProductDto.CreateDto dto) {
        Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setOnStock(dto.onStock());
        product.setImage(dto.image());
        product.setCategory(category);

        Product createdProduct = productRepository.save(product);
        return mapToResponseDto(createdProduct);
    }

    public ProductDto.ResponseDto update(Long id, ProductDto.EditDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setOnStock(dto.onStock());
        product.setImage(dto.image());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
    }

    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) return false;

        productRepository.deleteById(id);
        return true;
    }

    private ProductDto.ResponseDto mapToResponseDto(Product product) {
        return new ProductDto.ResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isOnStock(),
                product.getImage(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategory().getId()
        );
    }
}