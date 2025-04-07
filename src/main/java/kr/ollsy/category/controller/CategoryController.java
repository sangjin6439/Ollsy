package kr.ollsy.category.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import jakarta.validation.Valid;
import kr.ollsy.category.dto.request.CategoryRequest;
import kr.ollsy.category.dto.response.CategoryTreeResponse;
import kr.ollsy.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categorys")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryTreeResponse> createCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        CategoryTreeResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return ResponseEntity.created(URI.create("/api/v1/categorys/" + categoryResponse.getId())).body(categoryResponse);
    }
}
