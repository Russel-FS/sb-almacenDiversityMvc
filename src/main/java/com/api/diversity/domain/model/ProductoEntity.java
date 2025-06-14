package com.api.diversity.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.api.diversity.domain.enums.EstadoProducto;

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
import lombok.Data;

@Data
@Entity
@Table(name = "Productos")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Producto")
    private Long idProducto;

    @Column(name = "Codigo_Producto", nullable = false, unique = true)
    private String codigoProducto;

    @Column(name = "Nombre_Producto", nullable = false)
    private String nombreProducto;

    @Column(name = "Descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Categoria", nullable = false)
    private CategoryEntity categoria;

    @Column(name = "Precio_Compra", nullable = false)
    private BigDecimal precioCompra;

    @Column(name = "Precio_Venta", nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "Stock_Actual", nullable = false)
    private Integer stockActual;

    @Column(name = "Stock_Minimo", nullable = false)
    private Integer stockMinimo;

    @Column(name = "Stock_Maximo", nullable = false)
    private Integer stockMaximo;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "public_id")
    private String publicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoProducto estado;

    @CreationTimestamp
    @Column(name = "Fecha_Creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UsuarioEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private UsuarioEntity updatedBy;
}
