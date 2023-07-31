package com.kkoch.admin.api.controller.category;

import com.kkoch.admin.api.ApiResponse;
import com.kkoch.admin.api.controller.category.request.AddCategoryRequest;
import com.kkoch.admin.api.controller.category.request.SetCategoryRequest;
import com.kkoch.admin.api.controller.category.response.CategoryResponse;
import com.kkoch.admin.api.service.category.CategoryService;
import com.kkoch.admin.api.service.category.dto.AddCategoryDto;
import com.kkoch.admin.api.service.category.dto.SetCategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin-service/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<Long> addCategory(@Valid @RequestBody AddCategoryRequest request) {

        AddCategoryDto dto = request.toAddCategoryDto();
        Long result = categoryService.addCategory(dto);

        return ApiResponse.ok(result);
    }

    @GetMapping("/{parentId}")
    public ApiResponse<List<CategoryResponse>> getCategories(@PathVariable Long parentId) {
        return ApiResponse.ok(categoryService.getCategories(parentId));
    }

    @PatchMapping("/{categoryId}")
    public ApiResponse<CategoryResponse> setCategory(@PathVariable Long categoryId
            , @Valid @RequestBody SetCategoryRequest request) {

        SetCategoryDto dto = request.toSetCategoryDto();
        CategoryResponse result = categoryService.setCategory(categoryId, dto);
        return ApiResponse.of(HttpStatus.MOVED_PERMANENTLY, "카테고리가 수정되었습니다.", result);
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<Long> removeCategory(@PathVariable Long categoryId) {
        Long removeId = categoryService.removeCategory(categoryId);
        return ApiResponse.of(HttpStatus.MOVED_PERMANENTLY, "카테고리가 삭제되었습니다.", removeId);

    }
}
