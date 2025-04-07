package kr.ollsy.category.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeResponse {

    private Long id;
    private String name;
    private int depth;
    private List<CategoryTreeResponse> children;

    public static CategoryTreeResponse of(Category category) {
        return CategoryTreeResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .children(Collections.emptyList())
                .build();
    }
}
