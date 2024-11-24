package org.olzhas.projectnic.repository;

import org.olzhas.projectnic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("DELETE FROM Product p " +
            "WHERE p.id = :productId " +
            "AND p.user.id = :sellerId")
    int deleteByProductIdAndSellerId(@Param("productId") Long productId, @Param("sellerId") Long sellerId);

    @Query("SELECT p FROM Product p " +
            "WHERE p.user.id = :userId " +
            "AND p.id = :productId")
    Optional<Product> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}