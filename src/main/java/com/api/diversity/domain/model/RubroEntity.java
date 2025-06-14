package com.api.diversity.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Rubros")
public class RubroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Rubro")
    private Long idRubro;

    @Enumerated(EnumType.STRING)
    @Column(name = "Nombre_Rubro", nullable = false, unique = true)
    private NombreRubro nombreRubro;

    @Column(name = "Descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoRubro estado;

    @Column(name = "Fecha_Creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    public enum NombreRubro {
        Piñatería, Librería, Cámaras_de_Seguridad
    }

    public enum EstadoRubro {
        Activo, Inactivo
    }
}