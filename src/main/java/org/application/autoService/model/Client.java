package org.application.autoService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    private String middleName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;
}
