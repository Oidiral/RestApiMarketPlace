package org.olzhas.projectnic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.olzhas.projectnic.entity.Cart;

import java.util.List;

/**
 * DTO for {@link Cart}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {
    private Long id;
    private Long userId;
    private List<Long> cartItemIds;
}