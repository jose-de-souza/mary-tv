package tv.marytv.video.mapper;

import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getIconUrl(),
                item.getVideoUrl(),
                item.getItemDate(),
                item.isNew(),
                item.isHeadline(),
                item.getParent() != null ? item.getParent().getId() : null,
                item.getCategory() != null ? item.getCategory().getId() : null,
                item.getEvent() != null ? item.getEvent().getId() : null,
                List.of() // Placeholder; use toDtoWithChildren for full
        );
    }

    public ItemDto toDtoWithChildren(Item item) {
        // Recursive mapping for children
        List<ItemDto> childrenDtos = item.getChildren().stream()
                .map(this::toDtoWithChildren)
                .toList();
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getIconUrl(),
                item.getVideoUrl(),
                item.getItemDate(),
                item.isNew(),
                item.isHeadline(),
                item.getParent() != null ? item.getParent().getId() : null,
                item.getCategory() != null ? item.getCategory().getId() : null,
                item.getEvent() != null ? item.getEvent().getId() : null,
                childrenDtos
        );
    }

    public Item toEntity(ItemUpsertDto dto) {
        Item item = new Item();
        updateEntityFromDto(dto, item);
        return item;
    }

    public void updateEntityFromDto(ItemUpsertDto dto, Item item) {
        item.setTitle(dto.title());
        item.setDescription(dto.description());
        item.setIconUrl(dto.iconUrl());
        item.setVideoUrl(dto.videoUrl());
        item.setItemDate(dto.itemDate());
        item.setNew(dto.isNew());
        item.setHeadline(dto.isHeadline());
        // Relations set in service
    }
}