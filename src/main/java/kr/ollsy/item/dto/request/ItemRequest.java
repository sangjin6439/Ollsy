package kr.ollsy.item.dto.request;

import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import kr.ollsy.category.domain.Category;
import kr.ollsy.item.domain.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ItemRequest(
        @NotNull
        @Length(min = 2, max = 20)
        String name,

        @NotNull
        String description,

        int price,

        int stock,

        Long categoryId,

        @NotNull
        @Size(min = 1, message = "최소 하나의 이미지 ID가 필요합니다.")
        List<Long> itemImageId
) {
}