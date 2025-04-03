package kr.ollsy.order.dto.request;

import java.util.List;

import kr.ollsy.order_item.dto.request.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> orderItemsList;

}
