package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.ProductDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.dal.repos.ProductRepository;
import hr.algebra.fishingstore.dal.services.storage.CloudinaryStorageService;
import hr.algebra.fishingstore.model.entities.Category;
import hr.algebra.fishingstore.model.entities.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    private static final String DEFAULT_IMAGE = "no-image_ojoavo";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryStorageService cloudinaryStorageService;

    public ProductDto.ResponseDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
        return modelMapper.map(product, ProductDto.ResponseDto.class);
    }

    public List<ProductDto.ResponseDto> getByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        ArrayList<ProductDto.ResponseDto> result = new ArrayList<>();

        for (Product product : products) {
            ProductDto.ResponseDto dto = modelMapper.map(product, ProductDto.ResponseDto.class);
            dto.setCategoryId(product.getCategory().getId());
            result.add(dto);
        }
        return result;
    }

    public List<ProductDto.ResponseDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ProductDto.ResponseDto.class))
                .toList();
    }

    public ProductDto.ResponseDto create(ProductDto.CreateDto dto, MultipartFile image) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        Product product = modelMapper.map(dto, Product.class);
        product.setId(null);
        product.setCategory(category);
        setImageOrDefault(image, product);

        return modelMapper.map(productRepository.save(product), ProductDto.ResponseDto.class);
    }

    private void setImageOrDefault(MultipartFile image, Product product) {
        if (image != null && !image.isEmpty()) {
            deleteExistingImage(product);
            product.setImagePath(cloudinaryStorageService.upload(image));
        } else if (product.getImagePath() == null) {
            product.setImagePath(DEFAULT_IMAGE);
        }
    }

    private void deleteExistingImage(Product product) {
        if (product.getImagePath() != null && !product.getImagePath().equals(DEFAULT_IMAGE))
            cloudinaryStorageService.delete(product.getImagePath());

    }

    public ProductDto.ResponseDto update(Long id, ProductDto.EditDto dto, MultipartFile image) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        product.setCategory(null);
        modelMapper.map(dto, product);
        product.setId(id);
        product.setCategory(category);
        setImageOrDefault(image, product);

        return modelMapper.map(productRepository.save(product), ProductDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product == null)
            return false;

        deleteExistingImage(product);
        productRepository.deleteById(id);
        return true;
    }
}