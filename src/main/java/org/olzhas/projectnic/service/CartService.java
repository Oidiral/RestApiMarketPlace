package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.CartDto;

public interface CartService {
    void addProductToCart(Long userId, Long productId, Integer quantity);

    void removeProductFromCart(Long userId, Long productId);

    void deleteCart(Long customerId);

    CartDto getCartForUser(Long userId);
}
