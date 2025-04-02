package kr.ollsy.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor //Builder패턴 사용을 위해
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private int price;

    public static ItemResponse of(
            Long id, String name, String description, int price
    ){
        return ItemResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }
}