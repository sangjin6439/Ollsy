package kr.ollsy.order.dto.response;

import java.util.List;

import kr.ollsy.order_item.dto.request.OrderItemRequest;
import kr.ollsy.order_item.dto.response.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private List<OrderItemResponse> orderItemResponseList;
    private int totalPrice;
}
