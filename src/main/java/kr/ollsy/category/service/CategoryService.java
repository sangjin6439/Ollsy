package kr.ollsy.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.category.domain.Category;
import kr.ollsy.category.dto.request.CategoryRequest;
import kr.ollsy.category.dto.response.CategoryResponse;
import kr.ollsy.category.dto.response.CategoryTreeResponse;
import kr.ollsy.category.repository.CategoryRepository;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category parent = findParent(categoryRequest.parentId());
        int depth = calculateDepth(parent);
        Category category = saveCategory(categoryRequest.name(), depth, parent);

        if(parent!=null){
            parent.addChild(category);
        }

        categoryRepository.save(category);

        return CategoryResponse.of(category);
    }

    private Category findParent(Long parentId) {
        if (parentId == 0) return null;
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.PARENT_NOT_FOUND));
    }

    private int calculateDepth(Category parent) {
        if (parent == null) return 0;
        return parent.getDepth() + 1;
    }

    private Category saveCategory(String name, int depth, Category parent) {
        return Category.builder()
                .name(name)
                .depth(depth)
                .parent(parent)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryTreeResponse> findCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryTreeResponse> categoryTreeResponseList = createCategoryTreeResponseList(categoryList);
        return categoryTreeResponseList;
    }

    private List<CategoryTreeResponse> createCategoryTreeResponseList(List<Category> categoryList) {
        return categoryList.stream()
                .filter(category -> category.getDepth() == 0)
                .map(c -> CategoryTreeResponse.of(c))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}