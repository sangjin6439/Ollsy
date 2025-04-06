package kr.ollsy.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import kr.ollsy.item.domain.Item;
import kr.ollsy.item.repository.ItemRepository;
import kr.ollsy.order.domain.Order;
import kr.ollsy.order.dto.request.OrderRequest;
import kr.ollsy.order.dto.response.OrderDetailResponse;
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
                    return OrderItem.of(null, item, orderItemRequest.getQuantity());
                })
                .toList();

        int totalPrice = orderItemList.stream()
                .mapToInt(o -> o.getItem().getPrice() * o.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItemList)
                .totalPrice(totalPrice)
                .build();

        orderItemList.forEach(o -> o.setOrder(order));

        order.setUser(user);
        user.addOrder(order);

        orderRepository.save(order);

        List<OrderItemResponse> orderItemResponseList = orderItemListToDto(orderItemList);

        return OrderResponse.builder()
                .id(order.getId())
                .orderItemResponseList(orderItemResponseList)
                .totalPrice(totalPrice)
                .build();
    }

    private List<OrderItemResponse> orderItemListToDto(List<OrderItem> orderItem) {
        return orderItem.stream().map(o -> OrderItemResponse.builder()
                        .name(o.getItem().getName())
                        .price(o.getItem().getPrice())
                        .quantity(o.getQuantity())
                        .build())
                .toList();
    }

    public OrderDetailResponse findOrder(String providerId, Long id) {
        Order order = orderRepository.findByIdAndUser_ProviderId(id, providerId).orElseThrow(() -> new IllegalArgumentException("유저의 주문 정보를 확인할 수 없습니다."));
        List<OrderItemResponse> orderItemResponse = orderItemListToDto(order.getOrderItems());
        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderItemResponseList(orderItemResponse)
                .totalPrice(order.getTotalPrice())
                .orderAt(order.getCreateAt())
                .build();
    }

    public List<OrderResponse> findOrders(String providerId) {
        User user = userRepository.findByProviderId(providerId);

        List<Order> orders = user.getOrders();

        return orders.stream()
                .map(o -> OrderResponse.builder()
                        .id(o.getId())
                        .orderItemResponseList(orderItemListToDto(o.getOrderItems()))
                        .totalPrice(o.getTotalPrice())
                        .build())
                .toList();
    }
}