package com.api.diversity.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Categorias")
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Categoria")
    private Long idCategoria;

    @Column(name = "Nombre_Categoria", nullable = false, length = 50)
    private String nombreCategoria;

    @Column(name = "Descripcion", columnDefinition = "TEXT")
    private String descripcion;
}