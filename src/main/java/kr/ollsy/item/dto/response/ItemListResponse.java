package kr.ollsy.item.dto.response;

import java.util.List;

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
    private String categoryName;
    private List<String> itemImageUrl;
}
