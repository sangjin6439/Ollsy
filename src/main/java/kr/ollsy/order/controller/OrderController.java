package kr.ollsy.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import kr.ollsy.auth.OAuth2UserInfo;
import kr.ollsy.auth.jwt.dto.CustomOAuth2User;
import kr.ollsy.order.dto.request.OrderRequest;
import kr.ollsy.order.dto.response.OrderDetailResponse;
import kr.ollsy.order.dto.response.OrderResponse;
import kr.ollsy.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody OrderRequest orderRequest
            ) {
        OrderResponse orderResponse = orderService.createOrder(user.getName(),orderRequest);
        return ResponseEntity.created(URI.create("/api/v1/order/"+orderResponse.getId())).body(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> findOrder(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(orderService.findOrder(user.getName(),id));
    }
}
