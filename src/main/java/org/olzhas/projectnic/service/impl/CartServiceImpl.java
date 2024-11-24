package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import org.olzhas.projectnic.dto.CartDto;
import org.olzhas.projectnic.entity.Cart;
import org.olzhas.projectnic.entity.CartItem;
import org.olzhas.projectnic.entity.Product;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.mapper.CartMapper;
import org.olzhas.projectnic.repository.CartItemRepository;
import org.olzhas.projectnic.repository.CartRepository;
import org.olzhas.projectnic.repository.ProductsRepository;
import org.olzhas.projectnic.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public void addProductToCart(Long userId, Long productId, Integer quantity) {
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Cart existingCart = cartRepository.findByUserId(userId);
        if (existingCart == null) {
            existingCart = Cart.builder()
                    .user(User.builder().id(userId).build())
                    .created(LocalDateTime.now())
                    .build();
            cartRepository.save(existingCart);
        } else {
            CartItem cartItem = cartItemRepository.findByCartId(existingCart.getId());
            if (cartItem == null) {
                cartItem = CartItem.builder()
                        .cart(existingCart)
                        .product(product)
                        .quantity(quantity)
                        .build();
                cartItemRepository.save(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            }
        }
        productsRepository.save(product);
    }



    @Override
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new NotFoundException("Cart not found");
        }
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (cartItem == null) {
            throw new NotFoundException("Cart item not found");
        }
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
    }

    @Override
    public void deleteCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new NotFoundException("Cart not found");
        }
        cartRepository.delete(cart);
    }

    @Override
    public CartDto getCartForUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new NotFoundException("Cart not found");
        }
        return cartMapper.toDto(cart);
    }
}
