package com.api.diversity.domain.model;

import java.time.LocalDateTime;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.api.diversity.domain.enums.EstadoUsuarioRubro;

@Entity
@Table(name = "Usuario_Rubros", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ID_Usuario", "ID_Rubro" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRubroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario_Rubro")
    private Long idUsuarioRubro;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "ID_Rubro", nullable = false)
    private RubroEntity rubro;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoUsuarioRubro estado = EstadoUsuarioRubro.ACTIVO;

    @Column(name = "Fecha_Asignacion", nullable = false)
    private LocalDateTime fechaAsignacion = LocalDateTime.now();
}