package kr.ollsy.category.dto.response;

import java.util.List;

import kr.ollsy.category.domain.Category;
import lombok.Builder;


@Builder
public record CategoryTreeResponse(
        Long id,
        String name,
        int depth,
        List<CategoryTreeResponse> children
) {
    public static CategoryTreeResponse of(Category category) {
        return CategoryTreeResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .children(category.getChildren().stream()
                        .map(c->CategoryTreeResponse.of(c)).toList())
                .build();
    }
}