package kr.hs.dgsw.esm.domain.order.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {
    @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다.")
    private List<OrderItemRequest> items;

    @Getter
    @NoArgsConstructor
    public static class OrderItemRequest {
        private Long productId;
        private int quantity;
    }
}
