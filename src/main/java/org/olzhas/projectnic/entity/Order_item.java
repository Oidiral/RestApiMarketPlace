package org.olzhas.projectnic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order_item {
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
