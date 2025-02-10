package org.olzhas.projectnic.repository;

import org.olzhas.projectnic.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT o " +
            "FROM OrderItem o " +
            "WHERE o.orders.id = :orderId")
    List<OrderItem> findByOrder_id(@Param("orderId") Long orderId);
}