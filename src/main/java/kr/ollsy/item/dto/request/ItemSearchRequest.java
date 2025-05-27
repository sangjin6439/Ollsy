package kr.ollsy.item.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public record ItemSearchRequest(
        String name,
        Long categoryId,
        int minPrice,
        int maxPrice
) {
}