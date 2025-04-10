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
public class CategoryResponse {

    private Long id;
    private String name;
    private int depth;

    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .build();
    }
}
