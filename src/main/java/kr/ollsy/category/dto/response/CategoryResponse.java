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