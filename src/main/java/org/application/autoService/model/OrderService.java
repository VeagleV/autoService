package org.application.autoService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_service", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "service_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
}
