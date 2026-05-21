package kr.hs.dgsw.esm.domain.order.entity;

import jakarta.persistence.*;
import kr.hs.dgsw.esm.domain.product.entity.Product;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Integer quantity;

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getTotalPrice() {
        return orderPrice * quantity;
    }

    public static OrderItem createOrderItem(Product product, int quantity) {
        product.reduceStock(quantity);
        return OrderItem.builder()
                .product(product)
                .orderPrice(product.getPrice())
                .quantity(quantity)
                .build();
    }
}
