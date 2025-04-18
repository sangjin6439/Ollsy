package kr.ollsy.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
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
                            .orElseThrow(() -> new CustomException(GlobalExceptionCode.ITEM_NOT_FOUND));
                    item.removeStock(orderItemRequest.getQuantity());
                    return OrderItem.of(item, orderItemRequest.getQuantity());
                })
                .collect(Collectors.toList());

        int totalPrice = orderItemList.stream()
                .mapToInt(o -> o.getItem().getPrice() * o.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItemList)
                .totalPrice(totalPrice)
                .build();

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
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrder(String providerId, Long id) {
        Order order = findOrderWithProviderId(providerId, id);
        List<OrderItemResponse> orderItemResponse = orderItemListToDto(order.getOrderItems());
        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderItemResponseList(orderItemResponse)
                .totalPrice(order.getTotalPrice())
                .orderAt(order.getCreateAt())
                .build();
    }

    private Order findOrderWithProviderId(String providerId, Long id) {
        return orderRepository.findByIdAndUser_ProviderId(id, providerId)
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.ORDER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrders(String providerId) {
        User user = userRepository.findByProviderId(providerId);

        List<Order> orders = user.getOrders();

        return orders.stream()
                .map(o -> OrderResponse.builder()
                        .id(o.getId())
                        .orderItemResponseList(orderItemListToDto(o.getOrderItems()))
                        .totalPrice(o.getTotalPrice())
                        .orderAt(o.getCreateAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(String providerId, Long id) {
        Order order = findOrderWithProviderId(providerId, id);
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.addStock(orderItem.getQuantity());
        }
        orderRepository.delete(order);
    }
}