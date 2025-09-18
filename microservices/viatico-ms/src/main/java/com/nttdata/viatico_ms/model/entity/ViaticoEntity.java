package com.nttdata.viatico_ms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "viaticos")
@Getter @Setter
public class ViaticoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long empleadoId;

    @Column(nullable = false)
    private Long proyectoId;

    public enum Estado { CREADO, EN_APROBACION, APROBADO, RECHAZADO }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.CREADO;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal montoEstimado;

    @Column(length = 3, nullable = false)
    private String moneda = "PEN";

    @Column(columnDefinition = "TEXT")
    private String motivo;
}
