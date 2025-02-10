package org.olzhas.projectnic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.olzhas.projectnic.entity.Orders;
import org.olzhas.projectnic.entity.Status;

import java.util.Date;
import java.util.List;

/**
 * DTO for {@link Orders}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrdersDto {
    private Long id;
    private Long userId;
    private List<Long> order_itemIds;
    private Status status;
    private Double total_price;
    private Date date;
}