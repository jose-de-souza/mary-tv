package tv.marytv.video.service;

import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.entity.Item;
import tv.marytv.video.exception.ItemHasChildrenException;
import tv.marytv.video.mapper.ItemMapper;
import tv.marytv.video.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAllWithChildren();
        return items.stream()
                .map(itemMapper::toDtoWithChildren)
                .collect(Collectors.toList());
    }

    public Page<ItemDto> getShows(Pageable pageable) {
        Page<Item> items = itemRepository.findShows(pageable);
        return items.map(itemMapper::toDto);
    }

    public Page<ItemDto> getShowsFiltered(Long categoryId, Date fromDate, Date toDate, String keyword, Boolean isHeadline, Pageable pageable) {
        Page<Item> items = itemRepository.findShowsFiltered(categoryId, fromDate, toDate, keyword, isHeadline, pageable);
        return items.map(itemMapper::toDto);
    }

    public Page<ItemDto> getHeadliners(Pageable pageable) {
        Page<Item> items = itemRepository.findHeadliners(pageable);
        return items.map(itemMapper::toDto);
    }

    public Page<ItemDto> getEpisodes(Long seriesId, Pageable pageable) {
        Page<Item> items = itemRepository.findByParentId(seriesId, pageable);
        return items.map(itemMapper::toDto);
    }

    public ItemDto createItem(ItemUpsertDto dto) {
        Item item = itemMapper.toEntity(dto);
        if (dto.parentId() != null) {
            Item parent = itemRepository.findById(dto.parentId()).orElseThrow(() -> new RuntimeException("Parent not found"));
            item.setParent(parent);
        }
        Item saved = itemRepository.save(item);
        return itemMapper.toDtoWithChildren(saved);
    }

    public ItemDto updateItem(Long id, ItemUpsertDto dto) {
        Item item = itemRepository.findByIdWithChildren(id).orElseThrow(() -> new RuntimeException("Item not found"));
        itemMapper.updateEntityFromDto(dto, item);
        if (dto.parentId() != null) {
            Item parent = itemRepository.findById(dto.parentId()).orElseThrow(() -> new RuntimeException("Parent not found"));
            item.setParent(parent);
        }
        Item updated = itemRepository.save(item);
        return itemMapper.toDtoWithChildren(updated);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findByIdWithChildren(id).orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getChildren().isEmpty()) {
            throw new ItemHasChildrenException(item.getTitle(), item.getChildren().size());
        }
        itemRepository.delete(item);
    }
}