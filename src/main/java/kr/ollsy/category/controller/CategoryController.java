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
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + categoryResponse.getId())).body(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryTreeResponse>> findCategories(
    ) {
        return ResponseEntity.ok(categoryService.findCategories());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("id") Long id
    ) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}