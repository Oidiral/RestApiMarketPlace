package org.olzhas.projectnic.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.olzhas.projectnic.entity.Product;

/**
 * DTO for {@link Product}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @NotNull(message = "Price must not be empty")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Quantity must not be empty")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    private Integer quantity;

    @NotNull
    @NotBlank
    private Long categoryId;

    @NotNull
    @NotBlank
    private Long userId;

    private boolean active;
}
