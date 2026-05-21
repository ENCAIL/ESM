package kr.hs.dgsw.esm.domain.cart.service;

import kr.hs.dgsw.esm.domain.cart.dto.request.AddCartRequest;
import kr.hs.dgsw.esm.domain.cart.dto.response.CartResponse;
import kr.hs.dgsw.esm.domain.cart.entity.Cart;
import kr.hs.dgsw.esm.domain.cart.entity.CartItem;
import kr.hs.dgsw.esm.domain.cart.repository.CartItemRepository;
import kr.hs.dgsw.esm.domain.cart.repository.CartRepository;
import kr.hs.dgsw.esm.domain.member.entity.Member;
import kr.hs.dgsw.esm.domain.member.repository.MemberRepository;
import kr.hs.dgsw.esm.domain.product.entity.Product;
import kr.hs.dgsw.esm.domain.product.repository.ProductRepository;
import kr.hs.dgsw.esm.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addCart(String email, AddCartRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(404, "사용자를 찾을 수 없습니다.", "MEMBER_NOT_FOUND"));
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(404, "상품을 찾을 수 없습니다.", "PRODUCT_NOT_FOUND"));

        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseGet(() -> cartRepository.save(Cart.createCart(member)));

        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                        item -> item.addQuantity(request.getQuantity()),
                        () -> cartItemRepository.save(CartItem.builder()
                                .cart(cart)
                                .product(product)
                                .quantity(request.getQuantity())
                                .build())
                );
    }

    public CartResponse getCart(String email) {
        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(() -> new BusinessException(404, "장바구니가 비어있습니다.", "CART_EMPTY"));

        List<CartResponse.CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> CartResponse.CartItemResponse.builder()
                        .cartItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subTotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        int totalPrice = itemResponses.stream().mapToInt(CartResponse.CartItemResponse::getSubTotal).sum();

        return CartResponse.builder()
                .items(itemResponses)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public void deleteCartItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }
}
