package com.fthon.save_track.event.controller;


import com.fthon.save_track.common.dto.CommonResponse;
import com.fthon.save_track.common.dto.ErrorResponse;
import com.fthon.save_track.common.response.APIResponse;
import com.fthon.save_track.common.response.ApiResponseBody;
import com.fthon.save_track.common.response.ApiResponseGenerator;
import com.fthon.save_track.common.response.MessageCode;
import com.fthon.save_track.event.application.dto.request.CreateCategoryRequest;
import com.fthon.save_track.event.application.dto.response.CategorySearchResponse;
import com.fthon.save_track.event.application.service.CategoryService;
import com.fthon.save_track.event.persistence.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        List<CategorySearchResponse> data = categoryService.getCategory().stream().map(CategorySearchResponse::of).toList();
        return ResponseEntity.ok(new CategoryListResponse(200, data));
    }



    public static class CategoryListResponse extends CommonResponse<List<CategorySearchResponse>> {
        public CategoryListResponse(int code, String message, List<CategorySearchResponse> data) {
            super(code, message, data);
        }

        public CategoryListResponse(int code, List<CategorySearchResponse> data) {
            super(code, data);
        }
    }
}