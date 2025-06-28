package com.api.diversity.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.diversity.domain.enums.EstadoRol;
import com.api.diversity.domain.enums.EstadoUsuario;
import com.api.diversity.domain.enums.EstadoUserRole;

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
import lombok.Data;

@Data
@Entity
@Table(name = "Roles")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Rol")
    private Long idRol;

    @Column(name = "Nombre_Rol", nullable = false, unique = true)
    private String nombreRol;

    @Column(name = "Descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoRol estado;

    @Column(name = "Fecha_Creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    public List<UsuarioEntity> getUsuariosActivos() {
        return userRoles.stream()
                .filter(ur -> ur.getEstado() == EstadoUserRole.Activo)
                .map(UserRoleEntity::getUsuario)
                .filter(usuario -> usuario.getEstado() == EstadoUsuario.Activo)
                .toList();
    }
}