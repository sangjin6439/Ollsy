package kr.ollsy.order.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kr.ollsy.order_item.dto.request.OrderItemRequest;
import kr.ollsy.order_item.dto.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        List<OrderItemResponse> orderItemResponseList,
        int totalPrice,
        LocalDateTime orderAt
) {
}