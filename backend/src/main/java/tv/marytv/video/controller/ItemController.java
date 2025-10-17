package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}