package kr.hs.dgsw.esm.domain.cart.entity;

import jakarta.persistence.*;
import kr.hs.dgsw.esm.domain.member.entity.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public static Cart createCart(Member member) {
        return Cart.builder()
                .member(member)
                .items(new ArrayList<>())
                .build();
    }
}
