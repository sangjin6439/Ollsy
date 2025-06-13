package kr.ollsy.category.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotNull
        @Size(min = 2)
        String name,

        @NotNull
        Long parentId
) {
}