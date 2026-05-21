package kr.hs.dgsw.esm.domain.order.dto.response;

import kr.hs.dgsw.esm.domain.order.entity.Order;
import kr.hs.dgsw.esm.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private OrderStatus status;
    private int totalPrice;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;

    @Getter
    @Builder
    public static class OrderItemResponse {
        private String productName;
        private int orderPrice;
        private int quantity;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .items(order.getItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .productName(item.getProduct().getName())
                                .orderPrice(item.getOrderPrice())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
