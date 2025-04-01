package kr.ollsy.item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemRequest {

    String name;
    String description;
    int price;
}