package org.application.autoService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_part", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"service_id", "part_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicePartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;
}
