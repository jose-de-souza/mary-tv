package tv.marytv.video.mapper;

import tv.marytv.video.dto.CategoryDto;
import tv.marytv.video.dto.CategoryUpsertDto;
import tv.marytv.video.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category toEntity(CategoryUpsertDto dto) {
        Category category = new Category();
        updateEntityFromDto(dto, category);
        return category;
    }

    public void updateEntityFromDto(CategoryUpsertDto dto, Category category) {
        category.setName(dto.name());
    }
}