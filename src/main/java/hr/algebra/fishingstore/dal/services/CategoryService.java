package hr.algebra.fishingstore.dal.services;

import hr.algebra.fishingstore.dal.dto.CategoryDto;
import hr.algebra.fishingstore.dal.repos.CategoryRepository;
import hr.algebra.fishingstore.model.entities.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    public static final String CATEGORY_NOT_FOUND = "Category not found";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryDto.ResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        return modelMapper.map(category, CategoryDto.ResponseDto.class);
    }

    public List<CategoryDto.ResponseDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c->modelMapper.map(c, CategoryDto.ResponseDto.class))
                .toList();
    }

    public CategoryDto.ResponseDto create(CategoryDto.CreateDto dto) {
        Category category = modelMapper.map(dto, Category.class);

        return modelMapper.map(categoryRepository.save(category), CategoryDto.ResponseDto.class);
    }

    public CategoryDto.ResponseDto update(Long id, CategoryDto.EditDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        modelMapper.map(dto, category);
        return modelMapper.map(categoryRepository.save(category), CategoryDto.ResponseDto.class);
    }

    public boolean delete(Long id) {
        if (!categoryRepository.existsById(id))
            return false;

        categoryRepository.deleteById(id);
        return true;
    }
}