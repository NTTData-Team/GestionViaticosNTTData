package com.nttdata.empleado_ms.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "empleados")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "user_id")
    private Long userId;
    @Column(nullable = false, unique = true, name = "code_employee")
    private String codeEmployee;
    @Column(nullable = false, length = 30)
    private String position;
    @Column(nullable = false, unique = true, name = "document_identity")
    private String documentIdentity;
    @Column(length = 15)
    private String phone;
    @Column(length = 50)
    private String address;
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private AreaEntity area;
}
