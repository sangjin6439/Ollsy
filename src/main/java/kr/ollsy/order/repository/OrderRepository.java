package kr.ollsy.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import kr.ollsy.order.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUser_ProviderId(Long orderId, String providerId);

    Page<Order> findByUserProviderId(String providerId, Pageable pageable);
}
