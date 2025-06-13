package kr.ollsy.order_item.dto.response;

import lombok.Builder;

@Builder
public record OrderItemResponse(
        String name,
        int price,
        int quantity) {
}
