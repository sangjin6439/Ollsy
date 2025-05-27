package kr.ollsy.order_item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record OrderItemRequest(
        Long itemId,
        int quantity
) {
}