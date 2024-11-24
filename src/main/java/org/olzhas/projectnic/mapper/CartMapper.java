package org.olzhas.projectnic.mapper;

import org.mapstruct.*;
import org.olzhas.projectnic.entity.Cart;
import org.olzhas.projectnic.dto.CartDto;
import org.olzhas.projectnic.entity.CartItem;
import org.olzhas.projectnic.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
    @Mapping(source = "userId", target = "user.id")
    Cart toEntity(CartDto cartDto);

    @Mapping(target = "cartItemIds", expression = "java(cartItemsToCartItemIds(cart.getCartItems()))")
    @Mapping(source = "user.id", target = "userId")
    CartDto toDto(Cart cart);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user")
    Cart partialUpdate(CartDto cartDto, @MappingTarget Cart cart);

    default User createUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    default List<Long> cartItemsToCartItemIds(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
    }
}