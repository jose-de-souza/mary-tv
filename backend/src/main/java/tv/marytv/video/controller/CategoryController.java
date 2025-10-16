package tv.marytv.video.controller;

import tv.marytv.video.dto.ApiResponse;
import tv.marytv.video.dto.CategoryDto;
import tv.marytv.video.dto.CategoryUpsertDto;
import tv.marytv.video.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAll() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse<>(true, 200, "All categories fetched", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching categories: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getById(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Category fetched successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Category not found", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error fetching category: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> create(@RequestBody CategoryUpsertDto categoryDto) {
        try {
            CategoryDto savedCategory = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, 201, "Category created successfully", savedCategory));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error creating category: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> update(@PathVariable Long id, @RequestBody CategoryUpsertDto categoryDto) {
        try {
            return categoryService.updateCategory(id, categoryDto)
                    .map(d -> ResponseEntity.ok(new ApiResponse<>(true, 200, "Category updated successfully", d)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, 404, "Category not found", null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error updating category: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            if (categoryService.deleteCategory(id)) {
                return ResponseEntity.ok(new ApiResponse<>(true, 200, "Category deleted successfully", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, 404, "Category not found", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, 400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, 500, "Error deleting category: " + e.getMessage(), null));
        }
    }
}