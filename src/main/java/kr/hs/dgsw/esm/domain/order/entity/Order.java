package kr.hs.dgsw.esm.domain.order.entity;

import jakarta.persistence.*;
import kr.hs.dgsw.esm.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Integer totalPrice;

    @CreatedDate
    private LocalDateTime orderDate;

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .items(orderItems)
                .build();
        orderItems.forEach(item -> item.setOrder(order));
        return order;
    }
}
