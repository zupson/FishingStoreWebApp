package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dtos.CategoryDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.model.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto.ResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapToResponseDto(category);
    }

    public List<CategoryDto.ResponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CategoryDto.ResponseDto create(CategoryDto.CreateDto dto) {
        Category category = new Category();

        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setImage(dto.image());

        Category createdCategory = categoryRepository.save(category);
        return mapToResponseDto(createdCategory);
    }

    public CategoryDto.ResponseDto update(Long id, CategoryDto.EditDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setImage(dto.image());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    public boolean delete(Long id) {
        if (!categoryRepository.existsById(id)) return false;

        categoryRepository.deleteById(id);
        return true;
    }

    private CategoryDto.ResponseDto mapToResponseDto(Category category) {
        return new CategoryDto.ResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImage(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}