package com.nttdata.gasto_ms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "gastos")
@Getter @Setter
public class GastoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viatico_id", nullable = false)
    private Long viaticoId;

    public enum Categoria {
        TRANSPORTE,
        ALOJAMIENTO,
        ALIMENTACION,
        OTROS
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(length = 3, nullable = false)
    private String moneda = "PEN";

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "comprobante_url")
    private String comprobanteUrl;

    private OffsetDateTime creadoEn;
    private OffsetDateTime actualizadoEn;

    @PrePersist
    void prePersist() {
        var now = OffsetDateTime.now();
        creadoEn = now;
        actualizadoEn = now;
    }
    @PreUpdate
    void preUpdate() {
        actualizadoEn = OffsetDateTime.now();
    }
}