package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Productos")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Producto")
    private Long idProducto;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Categoria", nullable = false)
    private CategoryEntity categoria;

    @Column(name = "Stock")
    private Integer stock;

    @Column(name = "Url_Imagen")
    private String urlImagen;

    @Column(name = "Public_Id")
    private String publicId;

    @Column(name = "Precio_Unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "Fecha_Registro")
    private LocalDateTime fechaRegistro;
}
