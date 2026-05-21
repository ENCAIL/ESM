package kr.hs.dgsw.esm.domain.order.service;

import kr.hs.dgsw.esm.domain.member.entity.Member;
import kr.hs.dgsw.esm.domain.member.repository.MemberRepository;
import kr.hs.dgsw.esm.domain.order.dto.request.OrderRequest;
import kr.hs.dgsw.esm.domain.order.dto.response.OrderResponse;
import kr.hs.dgsw.esm.domain.order.entity.Order;
import kr.hs.dgsw.esm.domain.order.entity.OrderItem;
import kr.hs.dgsw.esm.domain.order.repository.OrderRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long createOrder(String email, OrderRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(404, "사용자를 찾을 수 없습니다.", "MEMBER_NOT_FOUND"));

        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new BusinessException(404, "상품을 찾을 수 없습니다.", "PRODUCT_NOT_FOUND"));
                    return OrderItem.createOrderItem(product, itemRequest.getQuantity());
                })
                .collect(Collectors.toList());

        Order order = Order.createOrder(member, orderItems);
        return orderRepository.save(order).getId();
    }

    public List<OrderResponse> getMyOrders(String email) {
        return orderRepository.findByMemberEmailOrderByOrderDateDesc(email).stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        return orderRepository.findById(id)
                .map(OrderResponse::of)
                .orElseThrow(() -> new BusinessException(404, "주문을 찾을 수 없습니다.", "ORDER_NOT_FOUND"));
    }
}
