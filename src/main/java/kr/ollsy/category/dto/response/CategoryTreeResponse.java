package kr.ollsy.category.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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