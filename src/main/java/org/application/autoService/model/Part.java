package org.application.autoService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "part")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;
}
