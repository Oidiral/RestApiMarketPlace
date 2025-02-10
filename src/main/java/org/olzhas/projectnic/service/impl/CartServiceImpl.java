package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.CartDto;
import org.olzhas.projectnic.entity.Cart;
import org.olzhas.projectnic.entity.CartItem;
import org.olzhas.projectnic.entity.Product;
import org.olzhas.projectnic.entity.User;
import org.olzhas.projectnic.exception.BadRequestException;
import org.olzhas.projectnic.exception.NotFoundException;
import org.olzhas.projectnic.mapper.CartMapper;
import org.olzhas.projectnic.repository.CartItemRepository;
import org.olzhas.projectnic.repository.CartRepository;
import org.olzhas.projectnic.repository.ProductsRepository;
import org.olzhas.projectnic.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public void addProductToCart(Long userId, Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        Product product = productsRepository.findById(productId)
                .filter(p -> p.getQuantity() >= quantity)
                .orElseThrow(() -> new NotFoundException("Product not found or insufficient quantity in stock"));

        Cart existingCart = cartRepository.findByUserIdWithCartItem(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(User.builder().id(userId).build())
                            .created(LocalDateTime.now())
                            .build();
                    log.info("New cart created: {}", newCart);
                    return cartRepository.save(newCart);
                });
        Optional<CartItem> optionalCartItem = existingCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        CartItem cartItem;
        if (optionalCartItem.isPresent()) {
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }else{
            cartItem = CartItem.builder()
                    .cart(existingCart)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }
        log.info("Adding product to cart: {}", cartItem);
        cartItemRepository.save(cartItem);
    }


    @Override
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.delete(cartItem);
        }
        log.info("Removing product from cart: {}", cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("Cart not found"));
        cartRepository.delete(cart);
        log.info("Deleting cart: {}", cart);
    }

    @Override
    public CartDto getCartForUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("Cart not found")
        );
        log.info("Getting cart for: {}", cart);
        return cartMapper.toDto(cart);
    }
}
