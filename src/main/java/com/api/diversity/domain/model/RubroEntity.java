package com.api.diversity.domain.model;

import java.time.LocalDateTime;

import com.api.diversity.domain.enums.EstadoRubro;
import com.api.diversity.domain.enums.NombreRubro;

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

    @Column(name = "Nombre_Rubro", nullable = false, length = 80)
    private String nombreRubro;

    @Enumerated(EnumType.STRING)
    @Column(name = "Code", nullable = false)
    private NombreRubro code;

    @Column(name = "Descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoRubro estado;

    @Column(name = "Fecha_Creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;
}