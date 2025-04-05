package kr.ollsy.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.item.domain.Item;
import kr.ollsy.item.repository.ItemRepository;
import kr.ollsy.order.domain.Order;
import kr.ollsy.order.dto.request.OrderRequest;
import kr.ollsy.order.dto.response.OrderResponse;
import kr.ollsy.order.repository.OrderRepository;
import kr.ollsy.order_item.domain.OrderItem;
import kr.ollsy.order_item.dto.response.OrderItemResponse;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(String providerId, OrderRequest orderRequest) {

        User user = userRepository.findByProviderId(providerId);
        List<OrderItem> orderItemList = orderRequest.getOrderItemsList().stream()
                .map(orderItemRequest -> {
                    Item item = itemRepository.findById(orderItemRequest.getItemId())
                            .orElseThrow(() -> new IllegalArgumentException("item을 찾을 수 없습니다"));
                    item.removeStock(orderItemRequest.getQuantity());
                    return OrderItem.of(null,item, orderItemRequest.getQuantity());
                })
                .toList();

        int totalPrice = orderItemList.stream()
                .mapToInt(o->o.getItem().getPrice()*o.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItemList)
                .totalPrice(totalPrice)
                .build();

        orderItemList.forEach(o->o.setOrder(order));

        orderRepository.save(order);

        List<OrderItemResponse> orderItemResponseList = orderItemList.stream()
                .map(o-> OrderItemResponse.builder()
                        .name(o.getItem().getName())
                        .price(o.getItem().getPrice())
                        .quantity(o.getQuantity())
                        .build())
                        .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .orderItemResponseList(orderItemResponseList)
                .totalPrice(totalPrice)
                .build();
        }
}
