package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.api.diversity.domain.enums.EstadoSalida;
import com.api.diversity.domain.enums.TipoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Salidas")
@AllArgsConstructor
@NoArgsConstructor
public class SalidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Salida")
    private Long idSalida;

    @Column(name = "Numero_Documento", nullable = false)
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Cliente", nullable = false)
    private ClienteEntity cliente;

    @CreationTimestamp
    @Column(name = "Fecha_Salida", nullable = false, updatable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "Motivo_Salida")
    private String motivoSalida;

    @Column(name = "Total_Venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoSalida estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Usuario_Registro", nullable = false)
    private UsuarioEntity usuarioRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Usuario_Aprobacion")
    private UsuarioEntity usuarioAprobacion;

    @Column(name = "Fecha_Aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "Observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "salida", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    private List<DetalleSalidaEntity> detalles = new ArrayList<>();
}