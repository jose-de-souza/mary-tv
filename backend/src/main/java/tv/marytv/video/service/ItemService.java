package tv.marytv.video.service;

import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.entity.Item;
import tv.marytv.video.exception.ItemHasChildrenException;
import tv.marytv.video.mapper.ItemMapper;
import tv.marytv.video.repository.CategoryRepository;
import tv.marytv.video.repository.EventRepository;
import tv.marytv.video.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper, CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> getItems(Long categoryId, Long eventId, boolean isNew, boolean isHeadline, Pageable pageable) {
        return itemRepository.findFilteredTopLevelItems(categoryId, eventId, isNew, isHeadline, pageable)
                .map(itemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ItemDto> getItemById(Long id) {
        return itemRepository.findByIdWithChildren(id).map(itemMapper::toDto);
    }

    @Transactional
    public ItemDto createItem(ItemUpsertDto itemDto) {
        if (itemDto.title() == null || itemDto.title().isEmpty()) {
            throw new IllegalArgumentException("Item title is required");
        }
        Item item = itemMapper.toEntity(itemDto);
        if (itemDto.parentId() != null) {
            item.setParent(itemRepository.findById(itemDto.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent item not found")));
        }
        if (itemDto.categoryId() != null) {
            item.setCategory(categoryRepository.findById(itemDto.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
        }
        if (itemDto.eventId() != null) {
            item.setEvent(eventRepository.findById(itemDto.eventId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found")));
        }
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Transactional
    public Optional<ItemDto> updateItem(Long id, ItemUpsertDto itemDto) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    if (itemDto.title() != null && !itemDto.title().isEmpty()) {
                        existingItem.setTitle(itemDto.title());
                    }
                    existingItem.setDescription(itemDto.description());
                    existingItem.setIconUrl(itemDto.iconUrl());
                    existingItem.setVideoUrl(itemDto.videoUrl());
                    existingItem.setItemDate(itemDto.itemDate());
                    existingItem.setNew(itemDto.isNew());
                    existingItem.setHeadline(itemDto.isHeadline());
                    if (itemDto.parentId() != null) {
                        existingItem.setParent(itemRepository.findById(itemDto.parentId())
                                .orElseThrow(() -> new IllegalArgumentException("Parent item not found")));
                    }
                    if (itemDto.categoryId() != null) {
                        existingItem.setCategory(categoryRepository.findById(itemDto.categoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Category not found")));
                    }
                    if (itemDto.eventId() != null) {
                        existingItem.setEvent(eventRepository.findById(itemDto.eventId())
                                .orElseThrow(() -> new IllegalArgumentException("Event not found")));
                    }
                    Item updatedItem = itemRepository.save(existingItem);
                    return itemMapper.toDto(updatedItem);
                });
    }

    @Transactional
    public boolean deleteItem(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    long childCount = itemRepository.countByParentId(id);
                    if (childCount > 0) {
                        throw new ItemHasChildrenException(item.getTitle(), (int) childCount);
                    }
                    itemRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getChildren(Long parentId) {
        return itemMapper.toDtoList(itemRepository.findChildrenByParentId(parentId));
    }
}