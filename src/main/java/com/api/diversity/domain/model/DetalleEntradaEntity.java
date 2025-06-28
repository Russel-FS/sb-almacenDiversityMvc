package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.api.diversity.domain.enums.EstadoDetalleEntrada;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Detalle_Entrada")
@AllArgsConstructor
@NoArgsConstructor
public class DetalleEntradaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle_Entrada")
    private Long idDetalleEntrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Entrada", nullable = false)
    private EntradaEntity entrada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Producto", nullable = false)
    private ProductoEntity producto;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "Precio_Unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "Subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Usuario_Registro", nullable = false)
    private UsuarioEntity usuarioRegistro;

    @CreationTimestamp
    @Column(name = "Fecha_Registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoDetalleEntrada estado;
}