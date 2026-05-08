package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.model.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto.ResponseDto getById(Long id) {
        return mapToResponseDto(categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found")));
    }

    public List<CategoryDto.ResponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CategoryDto.ResponseDto create(CategoryDto.CreateDto dto) {
        Category category = new Category();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return mapToResponseDto(categoryRepository.save(category));
    }

    public CategoryDto.ResponseDto update(Long id, CategoryDto.EditDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return mapToResponseDto(categoryRepository.save(category));
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
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}