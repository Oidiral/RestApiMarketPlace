package org.olzhas.projectnic.repository;

import org.olzhas.projectnic.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c " +
            "WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH CartItem " +
            "WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithCartItem(@Param("userId") Long userId);
}