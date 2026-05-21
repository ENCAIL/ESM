package kr.hs.dgsw.esm.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private List<CartItemResponse> items;
    private int totalPrice;

    @Getter
    @Builder
    public static class CartItemResponse {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private int price;
        private int quantity;
        private int subTotal;
    }
}
