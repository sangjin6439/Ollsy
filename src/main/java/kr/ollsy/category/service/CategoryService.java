package kr.ollsy.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ollsy.category.domain.Category;
import kr.ollsy.category.dto.request.CategoryRequest;
import kr.ollsy.category.dto.response.CategoryTreeResponse;
import kr.ollsy.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryTreeResponse createCategory(CategoryRequest categoryRequest) {
        Category parent = findParent(categoryRequest.getParentId());
        int depth = calculateDepth(parent);
        Category category = saveCategory(categoryRequest.getName(),depth, parent);

        categoryRepository.save(category);

        return CategoryTreeResponse.of(category);
    }

    private Category saveCategory(String name, int depth, Category parent) {
        return Category.builder()
                .name(name)
                .depth(depth)
                .parent(parent)
                .build();
    }


    private Category findParent(Long parentId) {
        if (parentId == null) return null;
        return categoryRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("상위 카테고리가 없습니다."));
    }

    private int calculateDepth(Category parent) {
        if (parent == null) return 0;
        return parent.getDepth() + 1;
    }


}
