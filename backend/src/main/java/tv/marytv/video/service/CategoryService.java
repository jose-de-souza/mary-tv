package tv.marytv.video.service;

import tv.marytv.video.dto.CategoryDto;
import tv.marytv.video.dto.CategoryUpsertDto;
import tv.marytv.video.entity.Category;
import tv.marytv.video.mapper.CategoryMapper;
import tv.marytv.video.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto createCategory(CategoryUpsertDto dto) {
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    public CategoryDto updateCategory(Long id, CategoryUpsertDto dto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryMapper.updateEntityFromDto(dto, category);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}