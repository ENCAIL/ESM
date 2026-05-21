package kr.hs.dgsw.esm.domain.order.repository;

import kr.hs.dgsw.esm.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberEmailOrderByOrderDateDesc(String email);
}
