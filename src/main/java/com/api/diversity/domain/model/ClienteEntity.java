package com.api.diversity.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

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
@Table(name = "Clientes")
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Cliente")
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo_Cliente", nullable = false)
    private TipoCliente tipoCliente;

    @Column(name = "Nombre_Completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "Razon_Social")
    private String razonSocial;

    @Column(name = "RUC", unique = true)
    private String ruc;

    @Column(name = "DNI", unique = true)
    private String dni;

    @Column(name = "Direccion")
    private String direccion;

    @Column(name = "Telefono")
    private String telefono;

    @Column(name = "Email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoCliente estado;

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

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<SalidaEntity> ventas = new ArrayList<>();
}