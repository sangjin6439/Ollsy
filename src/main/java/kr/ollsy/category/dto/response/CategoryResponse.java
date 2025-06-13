package kr.ollsy.category.dto.response;

import kr.ollsy.category.domain.Category;
import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        int depth
) {
    public static CategoryResponse of(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDepth()
        );
    }
}