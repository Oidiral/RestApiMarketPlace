package org.olzhas.projectnic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", nullable = false, name = "users_id")
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItems;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private Double totalPrice;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

}
