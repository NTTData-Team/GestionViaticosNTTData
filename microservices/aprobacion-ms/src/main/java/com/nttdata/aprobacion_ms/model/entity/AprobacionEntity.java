package com.nttdata.aprobacion_ms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "aprobaciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"viatico_id","nivel"})
)@Getter @Setter
public class AprobacionEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viatico_id", nullable = false)
    private Long viaticoId;

    public enum Estado { PENDIENTE, APROBADO, RECHAZADO }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.PENDIENTE;

    @Column(nullable = false)
    private Integer nivel = 1;

    @Column(name = "aprobador_id")
    private Long aprobadorId;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private OffsetDateTime creadoEn;
    private OffsetDateTime actualizadoEn;
    private OffsetDateTime decididoEn;

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
