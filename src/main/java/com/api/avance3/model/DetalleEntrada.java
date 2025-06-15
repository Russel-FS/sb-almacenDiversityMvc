package com.example.avance3.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Detalle_Entrada")
public class DetalleEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Detalle_Entrada")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Entrada", nullable = false)
    private Entradas entrada;

    @Column(name = "ID_Producto", length = 10, nullable = false)
    private String idProducto;

    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "Subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Entradas getEntrada() { return entrada; }
    public void setEntrada(Entradas entrada) { this.entrada = entrada; }

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
