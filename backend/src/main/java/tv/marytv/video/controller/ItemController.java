package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.ItemDto;
import tv.marytv.video.dto.ItemUpsertDto;
import tv.marytv.video.exception.ItemHasChildrenException;
import tv.marytv.video.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ItemDto>>> getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(defaultValue = "false") boolean isNew,
            @RequestParam(defaultValue = "false") boolean isHeadline,
            Pageable pageable) {
        try {
            Page<ItemDto> items = itemService.getItems(categoryId, eventId, isNew, isHeadline, pageable);
            return ResponseEntity.ok(new ApiResponse<>(true, 200, "Items fetched successfully", items));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching items: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> getById(@PathVariable Long id) {
        try {
            return itemService.getItemById(id)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Item fetched successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Item not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching item: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemDto>> create(@RequestBody ItemUpsertDto itemDto) {
        try {
            ItemDto savedItem = itemService.createItem(itemDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, 201, "Item created successfully", savedItem));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error creating item: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto>> update(@PathVariable Long id, @RequestBody ItemUpsertDto itemDto) {
        try {
            return itemService.updateItem(id, itemDto)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Item updated successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Item not found", null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error updating item: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (itemService.deleteItem(id)) {
                return ResponseEntity.ok(new ApiResponse<>(true, 200, "Item deleted successfully", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, 404, "Item not found", null));
        } catch (ItemHasChildrenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error deleting item: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<ApiResponse<List<ItemDto>>> getChildren(@PathVariable Long id) {
        try {
            List<ItemDto> children = itemService.getChildren(id);
            return ResponseEntity.ok(new ApiResponse<>(true, 200, "Children fetched successfully", children));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching children: " + e.getMessage(), null));
        }
    }
}