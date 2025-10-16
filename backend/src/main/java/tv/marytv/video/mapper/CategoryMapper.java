package tv.marytv.video.mapper;

import tv.marytv.video.dto.CategoryDto;
import tv.marytv.video.dto.CategoryUpsertDto;
import tv.marytv.video.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryUpsertDto categoryUpsertDto);
}