package com.api.diversity.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.*; // Solo si usas JPA
import lombok.Data;

@Data
@Entity
@Table(name = "Detalle_Pedido")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle")
    private Integer ID_Detalle;

    @Column(name = "ID_Pedido", nullable = false)
    private Integer ID_Pedido;

    @Column(name = "ID_Producto", nullable = false)
    private String ID_Producto;

    @Column(name = "Cantidad", nullable = false)
    private Integer Cantidad;

    @Column(name = "Subtotal", nullable = false)
    private BigDecimal Subtotal;
}