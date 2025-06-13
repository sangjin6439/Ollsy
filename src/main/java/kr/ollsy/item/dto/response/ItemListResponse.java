package kr.ollsy.item.dto.response;

import java.util.List;

import lombok.Builder;

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