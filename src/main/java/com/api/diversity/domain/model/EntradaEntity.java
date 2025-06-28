package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.api.diversity.domain.enums.EstadoEntrada;

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
@Table(name = "Entradas")
@AllArgsConstructor
@NoArgsConstructor
public class EntradaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Entrada")
    private Long idEntrada;

    @Column(name = "Numero_Factura", nullable = false)
    private String numeroFactura;

    @Column(name = "Tipo_Documento", nullable = false)
    private String tipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Proveedor", nullable = false)
    private ProveedorEntity proveedor;

    @CreationTimestamp
    @Column(name = "Fecha_Entrada", nullable = false, updatable = false)
    private LocalDateTime fechaEntrada;

    @Column(name = "Costo_Total", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoEntrada estado;

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

    @OneToMany(mappedBy = "entrada", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    private List<DetalleEntradaEntity> detalles = new ArrayList<>();
}