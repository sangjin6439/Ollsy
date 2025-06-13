package kr.ollsy.item.dto.request;

public record ItemSearchRequest(
        String name,
        Long categoryId,
        int minPrice,
        int maxPrice
) {
}