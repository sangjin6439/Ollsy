package kr.ollsy.order_item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequest {
    private Long itemId;
    private int quantity;
}
