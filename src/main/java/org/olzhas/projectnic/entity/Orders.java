package org.olzhas.projectnic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "users_id")
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "orders")
    private List<Order_item> order_items;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private Double total_price;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

}
