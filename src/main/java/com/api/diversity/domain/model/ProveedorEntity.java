package com.api.diversity.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.api.diversity.domain.enums.EstadoProveedor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Proveedores")
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Proveedor")
    private Long idProveedor;

    @Column(name = "Razon_Social", nullable = false)
    private String razonSocial;

    @Column(name = "RUC", nullable = false, unique = true)
    private String ruc;

    @Column(name = "Direccion")
    private String direccion;

    @Column(name = "Telefono")
    private String telefono;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "Representante_Legal")
    private String representanteLegal;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoProveedor estado;

    @CreationTimestamp
    @Column(name = "Fecha_Creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<EntradaEntity> entradas = new ArrayList<>();
}