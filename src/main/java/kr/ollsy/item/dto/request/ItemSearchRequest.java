package kr.ollsy.item.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemSearchRequest {
    private String name;
    private Long categoryId;
    private int minPrice;
    private int maxPrice;
}
