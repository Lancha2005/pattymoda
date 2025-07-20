package com.pattymoda.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_impuestos")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfiguracionImpuesto extends BaseEntity {
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name = "porcentaje", precision = 5, scale = 2, nullable = false)
    private BigDecimal porcentaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoImpuesto tipo = TipoImpuesto.OTROS;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "aplicar_por_defecto")
    private Boolean aplicarPorDefecto = false;

    @Column(name = "aplica_a_productos")
    private Boolean aplicaAProductos = true;

    @Column(name = "aplica_a_servicios")
    private Boolean aplicaAServicios = true;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "base_legal", length = 500)
    private String baseLegal;

    @Column(name = "fecha_vigencia_inicio")
    private LocalDate fechaVigenciaInicio;

    @Column(name = "fecha_vigencia_fin")
    private LocalDate fechaVigenciaFin;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creadoPor;

    public enum TipoImpuesto {
        IGV, ISC, MUNICIPAL, OTROS
    }
} 