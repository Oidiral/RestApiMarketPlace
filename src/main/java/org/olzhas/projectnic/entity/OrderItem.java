package org.olzhas.projectnic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "orders_id")
    private Orders orders;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "products_id")
    private Product product;
    @Column(nullable = false)
    private int quantity;
    private Double price;
}
