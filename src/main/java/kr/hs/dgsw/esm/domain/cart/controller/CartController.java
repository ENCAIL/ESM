package kr.hs.dgsw.esm.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hs.dgsw.esm.domain.cart.dto.request.AddCartRequest;
import kr.hs.dgsw.esm.domain.cart.dto.response.CartResponse;
import kr.hs.dgsw.esm.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "장바구니 관련 API")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 새로운 상품을 추가합니다. 이미 있는 상품이면 수량이 합산됩니다.")
    @PostMapping
    public void addCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid AddCartRequest request) {
        cartService.addCart(userDetails.getUsername(), request);
    }

    @Operation(summary = "장바구니 조회", description = "현재 사용자의 장바구니 상품 목록과 총 금액을 조회합니다.")
    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return cartService.getCart(userDetails.getUsername());
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 특정 아이템을 삭제합니다.")
    @DeleteMapping("/items/{itemId}")
    public void deleteCartItem(@PathVariable Long itemId) {
        cartService.deleteCartItem(itemId);
    }
}
