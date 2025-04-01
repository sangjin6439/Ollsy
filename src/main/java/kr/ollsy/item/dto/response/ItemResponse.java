package kr.ollsy.item.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponse {
    Long id;
    String name;
    String description;
    int price;
}