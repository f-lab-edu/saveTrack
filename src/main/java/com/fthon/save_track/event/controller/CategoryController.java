package com.fthon.save_track.event.controller;


import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.event.application.dto.request.CreateCategoryRequest;
import com.fthon.save_track.event.application.dto.response.CategorySearchResponse;
import com.fthon.save_track.event.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "카테고리 관련 API", description = "카테고리 관련 API")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
            content = @Content(schema = @Schema(implementation = CategoryListResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping()
    public ResponseEntity<CategoryListResponse> getList() {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public void createCategory(
            @RequestBody CreateCategoryRequest request
    ) {

    }

    public static class CategoryListResponse extends CommonResponse<List<CategorySearchResponse>> {
        public CategoryListResponse(List<CategorySearchResponse> data) {
            super(data);
        }
    }
}