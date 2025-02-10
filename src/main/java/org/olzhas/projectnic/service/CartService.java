package org.olzhas.projectnic.service;

import org.olzhas.projectnic.dto.CartDto;


public interface CartService {
    void addProductToCart(Long userId, Long productId, Integer quantity);

    void removeProductFromCart(Long userId, Long productId);

    void clearCart(Long userId);

    CartDto getCartForUser(Long userId);
}
