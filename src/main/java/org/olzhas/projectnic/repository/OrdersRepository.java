package org.olzhas.projectnic.repository;

import org.aspectj.weaver.ast.Or;
import org.olzhas.projectnic.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o " +
            "FROM Orders o " +
            "WHERE o.user.id = :userId ")
    List<Orders> findByUserId(@Param("userId") Long userId);
    @Query("SELECT o " +
            "FROM Orders o " +
            "JOIN FETCH o.orderItems " +
            "WHERE o.id = :orderId")
    Orders findOrderWithItems(@Param("orderId") Long orderId);

    @Query("SELECT o " +
            "FROM Orders o " +
            "WHERE o.user.id = :userId")
    Page<Orders> findAllByUserId(Pageable pageable,
                                          @Param("userId") Long userId);
}