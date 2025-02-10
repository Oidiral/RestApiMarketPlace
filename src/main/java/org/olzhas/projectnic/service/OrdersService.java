package org.olzhas.projectnic.service;


import org.olzhas.projectnic.dto.OrdersDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersService {
    OrdersDto createOrder(long userId);

    OrdersDto cancelOrder(long orderId, long userId);

    Page<OrdersDto> getUserOrders(Pageable pageable, Long userId);

    OrdersDto getOrder(long orderId);
}
