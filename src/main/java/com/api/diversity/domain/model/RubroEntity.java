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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Rubros")
public class RubroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdRubro")
    private Long idRubro;

    @Column(name = "NombreRubro", nullable = false)
    private String nombreRubro;

    @Enumerated(EnumType.STRING)
    @Column(name = "Code", nullable = false)
    private NombreRubro code;

    @Column(name = "Descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoRubro estado;

    @Column(name = "PublicId")
    private String publicId;

    @Column(name = "ImagenUrl")
    private String imagenUrl;

    @Column(name = "Fecha_Creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UsuarioEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UsuarioEntity updatedBy;
}