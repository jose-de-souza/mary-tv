package tv.marytv.video.mapper;

import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "children", expression = "java(item.getChildren() != null ? item.getChildren().stream().map(this::toDto).collect(java.util.stream.Collectors.toList()) : java.util.List.of())")
    @Mapping(target = "parentId", expression = "java(item.getParent() != null ? item.getParent().getId() : null)")
    @Mapping(target = "categoryId", expression = "java(item.getCategory() != null ? item.getCategory().getId() : null)")
    @Mapping(target = "eventId", expression = "java(item.getEvent() != null ? item.getEvent().getId() : null)")
    ItemDto toDto(Item item);

    List<ItemDto> toDtoList(List<Item> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item toEntity(ItemUpsertDto itemUpsertDto);
}