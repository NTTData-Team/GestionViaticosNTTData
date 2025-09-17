package com.nttdata.empleado_ms.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proyectos")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    @Column(length = 100)
    private String description;
    private Boolean active;
}
