package kr.ollsy.item.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record ItemResponse(
        Long id,
        String name,
        String description,
        int price,
        int stock,
        String categoryName,
        List<String> itemImageUrl
) {
    public static ItemResponse of(
            Long id, String name, String description, int price, int stock, String categoryName, List<String> itemImageUrl
    ) {
        return ItemResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .categoryName(categoryName)
                .itemImageUrl(itemImageUrl)
                .build();
    }
}