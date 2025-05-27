package kr.ollsy.category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ollsy.category.dto.request.CategoryRequest;
import kr.ollsy.category.dto.response.CategoryResponse;
import kr.ollsy.category.dto.response.CategoryTreeResponse;
import kr.ollsy.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Category")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 생성 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "올바른 카테고리 정보를 입력해 주세요", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + categoryResponse.id())).body(categoryResponse);
    }

    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 완료", content = {@Content(mediaType = "application/json")}),
    })
    public ResponseEntity<List<CategoryTreeResponse>> findCategories(
    ) {
        return ResponseEntity.ok(categoryService.findCategories());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("id") Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}