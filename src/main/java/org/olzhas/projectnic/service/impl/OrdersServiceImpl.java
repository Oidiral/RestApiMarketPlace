package org.olzhas.projectnic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olzhas.projectnic.dto.OrdersDto;
import org.olzhas.projectnic.entity.*;
import org.olzhas.projectnic.exception.AccessDeniedException;
import org.olzhas.projectnic.exception.BadRequestException;
import org.olzhas.projectnic.mapper.OrdersMapper;
import org.olzhas.projectnic.repository.CartRepository;
import org.olzhas.projectnic.repository.OrderItemRepository;
import org.olzhas.projectnic.repository.OrdersRepository;
import org.olzhas.projectnic.repository.ProductsRepository;
import org.olzhas.projectnic.service.CartService;
import org.olzhas.projectnic.service.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final OrdersMapper ordersMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrdersDto createOrder(long userId) {
        Cart cart = cartRepository.findByUserIdWithCartItem(userId).orElseThrow(
                () -> new BadRequestException("Cart not found")
        );
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        Orders order = Orders.builder()
                .user(User.builder()
                        .id(userId)
                        .build())
                .date(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
        cart.getCartItems().forEach(cartItem -> {
            productsRepository.decreaseQuantity(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity()
            );
            OrderItem item = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .orders(order)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                    .build();

            orderItems.add(item);
        });
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        order.setTotalPrice(orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum());
        cartService.clearCart(userId);
        log.info("Orders created");
        return ordersMapper.toDto(order);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public OrdersDto cancelOrder(long orderId, long userId) {
        Orders orders = ordersRepository.findOrderWithItems(orderId);
        if (orders.getStatus() == Status.CANCELLED) {
            throw new BadRequestException("Order already cancelled");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !orders.getUser().getUsername().equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Access denied");
        }
        List<Product> updateProduct = orders.getOrderItems().stream()
                .map(orderItem -> {
                    Product product = orderItem.getProduct();
                    product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                    return product;
                }).toList();
        productsRepository.saveAll(updateProduct);
        orders.setStatus(Status.CANCELLED);
        orderItemRepository.deleteAll(orders.getOrderItems());
        ordersRepository.save(orders);
        log.info("Orders canceled");
        return ordersMapper.toDto(orders);
    }

    @Override
    public Page<OrdersDto> getUserOrders(Pageable pageable, Long userId) {
        return ordersRepository.findAllByUserId(pageable, userId)
                .map(ordersMapper::toDto);
    }

    @Override
    public OrdersDto getOrder(long orderId) {
        return ordersMapper.toDto(ordersRepository.findById(orderId).orElseThrow(
                () -> new BadRequestException("Order not found")
        ));
    }
}
