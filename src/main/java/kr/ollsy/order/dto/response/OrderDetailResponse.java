package kr.ollsy.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import kr.ollsy.order_item.dto.response.OrderItemResponse;
import lombok.Builder;

@Builder
public record OrderDetailResponse(
        Long id,
        List<OrderItemResponse> orderItemResponseList,
        int totalPrice,
        LocalDateTime orderAt
) {
}