package com.api.diversity.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.api.diversity.domain.enums.EstadoRubro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Rubros", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_Rubros_Code_Nombre", columnNames = { "Code", "Nombre" })
})
public class RubroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Rubro")
    private Long idRubro;

    @Column(name = "Nombre", nullable = false, length = 80)
    private String nombreRubro;

    @Column(name = "Code", nullable = false, length = 50)
    private String code;

    @Column(name = "Descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoRubro estado;

    @Column(name = "PublicId", length = 100)
    private String publicId;

    @Column(name = "ImagenUrl", length = 255)
    private String imagenUrl;

    @CreationTimestamp
    @Column(name = "Fecha_Creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;
}