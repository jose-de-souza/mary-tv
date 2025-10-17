package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Import Pageable
// Remove PageRequest import if present
import org.springframework.format.annotation.DateTimeFormat; // Import DateTimeFormat
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant; // Import Instant or ZonedDateTime
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Keep getAllItems as is
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDto>>> getAllItems() {
        List<ItemDto> items = itemService.getAllItems();
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Items retrieved", items));
    }

    // Keep getShows as is (using default Spring Pageable resolution)
    @GetMapping("/shows")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getShows(Pageable pageable) {
        Page<ItemDto> shows = itemService.getShows(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Shows retrieved", shows));
    }

    // Corrected getShowsFiltered
    @GetMapping("/shows/filtered")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getShowsFiltered(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate, // Use Instant or ZonedDateTime with ISO format
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,   // Use Instant or ZonedDateTime with ISO format
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isHeadline,
            Pageable pageable) { // Let Spring inject Pageable directly

        // Convert Instant to Date before passing to service/repository
        Date fromDateAsDate = (fromDate != null) ? Date.from(fromDate) : null;
        Date toDateAsDate = (toDate != null) ? Date.from(toDate) : null;

        Page<ItemDto> shows = itemService.getShowsFiltered(categoryId, fromDateAsDate, toDateAsDate, keyword, isHeadline, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Filtered shows retrieved", shows));
    }

    // Keep getHeadliners as is (using default Spring Pageable resolution)
    @GetMapping("/headliners")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getHeadliners(Pageable pageable) {
        Page<ItemDto> headliners = itemService.getHeadliners(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Headliners retrieved", headliners));
    }

    // Keep getEpisodes as is (using default Spring Pageable resolution)
    @GetMapping("/series/{seriesId}/episodes")
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getEpisodes(
            @PathVariable Long seriesId,
            Pageable pageable) {
        Page<ItemDto> episodes = itemService.getEpisodes(seriesId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Episodes retrieved", episodes));
    }

    // Keep createItem as is
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ItemDto>> createItem(@RequestBody ItemUpsertDto dto) {
        ItemDto item = itemService.createItem(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 201, "Item created", item));
    }

    // Keep updateItem as is
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ItemDto>> updateItem(@PathVariable Long id, @RequestBody ItemUpsertDto dto) {
        ItemDto updated = itemService.updateItem(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Item updated", updated));
    }

    // Keep deleteItem as is
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Item deleted", null));
    }

    // Remove the manual parseDate method, Spring handles it now
}