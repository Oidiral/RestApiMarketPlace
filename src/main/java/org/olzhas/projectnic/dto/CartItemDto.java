package org.olzhas.projectnic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.olzhas.projectnic.entity.CartItem;

/**
 * DTO for {@link CartItem}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDto {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
}