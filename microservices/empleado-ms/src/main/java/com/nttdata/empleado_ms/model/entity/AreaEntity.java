package com.nttdata.empleado_ms.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areas")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AreaEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50,nullable = false, unique = true)
    private String name;
}
