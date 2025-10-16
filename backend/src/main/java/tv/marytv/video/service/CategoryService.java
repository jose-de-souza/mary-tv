package tv.marytv.video.service;

import tv.marytv.video.dto.CategoryDto;
import tv.marytv.video.dto.CategoryUpsertDto;
import tv.marytv.video.entity.Category;
import tv.marytv.video.mapper.CategoryMapper;
import tv.marytv.video.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Transactional
    public CategoryDto createCategory(CategoryUpsertDto categoryDto) {
        if (categoryDto.name() == null || categoryDto.name().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public Optional<CategoryDto> updateCategory(Long id, CategoryUpsertDto categoryDto) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    if (categoryDto.name() != null && !categoryDto.name().isEmpty()) {
                        existingCategory.setName(categoryDto.name());
                    }
                    Category updatedCategory = categoryRepository.save(existingCategory);
                    return categoryMapper.toDto(updatedCategory);
                });
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}