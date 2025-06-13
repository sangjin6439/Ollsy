package kr.ollsy.order_item.dto.request;

public record OrderItemRequest(
        Long itemId,
        int quantity
) {
}