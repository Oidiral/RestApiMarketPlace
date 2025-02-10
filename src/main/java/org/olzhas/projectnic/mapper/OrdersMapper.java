package org.olzhas.projectnic.mapper;

import org.mapstruct.*;
import org.olzhas.projectnic.dto.OrdersDto;
import org.olzhas.projectnic.entity.OrderItem;
import org.olzhas.projectnic.entity.Orders;
import org.olzhas.projectnic.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrdersMapper {
    @Mapping(source = "userId", target = "user.id")
    Orders toEntity(OrdersDto ordersDto);

    @Mapping(target = "order_itemIds", expression = "java(order_itemsToOrder_itemIds(orders.getOrder_items()))")
    @Mapping(source = "user.id", target = "userId")
    OrdersDto toDto(Orders orders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user")
    Orders partialUpdate(OrdersDto ordersDto, @MappingTarget Orders orders);

    default User createUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    default List<Long> order_itemsToOrder_itemIds(List<OrderItem> order_items) {
        return order_items.stream().map(OrderItem::getId).collect(Collectors.toList());
    }
}