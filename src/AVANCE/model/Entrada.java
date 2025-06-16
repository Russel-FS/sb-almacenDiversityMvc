package com.example.AVANCE.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Entradas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"Numero_Factura", "ID_Proveedor"})
})
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long id;

    @Column(name = "numero_factura", nullable = false)
    private String numeroFactura;

    @Column(name = "id_proveedor", nullable = false)
    private Long idProveedor;

    @Column(name = "fecha_entrada")
    private LocalDateTime fechaEntrada = LocalDateTime.now();

    @Column(name = "costo_total", nullable = false)
    private BigDecimal costoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado = Estado.Pendiente;

    @Column(name = "id_usuario_registro", nullable = false)
    private Long idUsuarioRegistro;

    @Column(name = "id_usuario_aprobacion")
    private Long idUsuarioAprobacion;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    public enum Estado {
        Pendiente,
        Completado,
        Anulado
    }
    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(Long idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public Long getIdUsuarioAprobacion() {
        return idUsuarioAprobacion;
    }

    public void setIdUsuarioAprobacion(Long idUsuarioAprobacion) {
        this.idUsuarioAprobacion = idUsuarioAprobacion;
    }

    public LocalDateTime getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


}
