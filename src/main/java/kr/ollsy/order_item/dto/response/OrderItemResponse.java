package kr.ollsy.order_item.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record OrderItemResponse(
        String name,
        int price,
        int quantity) {
}
