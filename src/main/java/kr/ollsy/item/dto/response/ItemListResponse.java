package kr.ollsy.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
}
