package kr.ollsy.order.controller;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.ollsy.auth.jwt.dto.CustomOAuth2User;
import kr.ollsy.order.dto.request.OrderRequest;
import kr.ollsy.order.dto.response.OrderDetailResponse;
import kr.ollsy.order.dto.response.OrderResponse;
import kr.ollsy.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문을 생성합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 요청했습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        OrderResponse orderResponse = orderService.createOrder(user.getName(), orderRequest);
        return ResponseEntity.created(URI.create("/api/v1/order/" + orderResponse.id())).body(orderResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 상세 정보 확인", description = "주문 상세 정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주문 정보를 확인합니다", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 요청했습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<OrderDetailResponse> findOrder(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(orderService.findOrder(user.getName(), id));
    }

    @GetMapping
    @Operation(summary = "모든 주문 정보 확인", description = "유저의 모든 주문 정보를 확인합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주문 확인 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 요청했습니다", content = @Content(mediaType = "application/json")),
    })
    @PageableAsQueryParam
    public ResponseEntity<Page<OrderResponse>> findOrders(
            @Parameter(hidden = true)
            @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return ResponseEntity.ok(orderService.findOrders(user.getName(), pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "주문 취소", description = "주문을 취소합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 취소 완료", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 유저가 요청했습니다", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal CustomOAuth2User user,
            @PathVariable("id") Long id
    ) {
        orderService.cancelOrder(user.getName(), id);
        return ResponseEntity.noContent().build();
    }
}