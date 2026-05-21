package kr.hs.dgsw.esm.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hs.dgsw.esm.domain.order.dto.request.OrderRequest;
import kr.hs.dgsw.esm.domain.order.dto.response.OrderResponse;
import kr.hs.dgsw.esm.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "주문 관리 관련 API")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "상품들을 주문합니다. 주문 시 상품 재고가 차감됩니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid OrderRequest request) {
        return orderService.createOrder(userDetails.getUsername(), request);
    }

    @Operation(summary = "내 주문 이력 조회", description = "현재 로그인한 사용자의 모든 주문 이력을 최신순으로 조회합니다.")
    @GetMapping
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderService.getMyOrders(userDetails.getUsername());
    }

    @Operation(summary = "주문 상세 조회", description = "주문 ID를 통해 특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }
}
