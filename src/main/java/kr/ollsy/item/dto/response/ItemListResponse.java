package kr.ollsy.item.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record ItemListResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        String categoryName,
        List<String> itemImageUrl
) {
}