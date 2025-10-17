package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDto>>> getAllItems() {
        List<ItemDto> items = itemService.getAllItems();
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Items retrieved", items));
    }

    @GetMapping("/shows")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getShows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> shows = itemService.getShows(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Shows retrieved", shows));
    }

    @GetMapping("/shows/filtered")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getShowsFiltered(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isHeadline,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> shows = itemService.getShowsFiltered(categoryId, parseDate(fromDate), parseDate(toDate), keyword, isHeadline, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Filtered shows retrieved", shows));
    }

    @GetMapping("/headliners")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getHeadliners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> headliners = itemService.getHeadliners(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Headliners retrieved", headliners));
    }

    @GetMapping("/series/{seriesId}/episodes")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getEpisodes(
            @PathVariable Long seriesId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> episodes = itemService.getEpisodes(seriesId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Episodes retrieved", episodes));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ItemDto>> createItem(@RequestBody ItemUpsertDto dto) {
        ItemDto item = itemService.createItem(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 201, "Item created", item));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ItemDto>> updateItem(@PathVariable Long id, @RequestBody ItemUpsertDto dto) {
        ItemDto updated = itemService.updateItem(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Item updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Item deleted", null));
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        // Simple parse; use DateTimeFormatter in prod
        return Date.from(java.time.Instant.parse(dateStr));
    }
}