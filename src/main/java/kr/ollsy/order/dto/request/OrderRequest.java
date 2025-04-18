package kr.ollsy.order.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import kr.ollsy.order_item.dto.request.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    @NotEmpty(message = "주문 항목은 비어있을 수 없습니다.")
    private List<OrderItemRequest> orderItemList = new ArrayList<>();
}
