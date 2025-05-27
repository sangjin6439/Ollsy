package kr.ollsy.category.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record CategoryRequest(
        @NotNull
        @Size(min = 2)
        String name,

        @NotNull
        Long parentId
) {
}