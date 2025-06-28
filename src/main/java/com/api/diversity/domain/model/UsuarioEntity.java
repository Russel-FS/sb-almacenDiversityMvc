package com.api.diversity.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario")
    private Long idUsuario;

    @Column(name = "Nombre_Usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Nombre_Completo", nullable = false)
    private String nombreCompleto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Rubro", nullable = false)
    private RubroEntity rubro;

    @Column(name = "Contraseña", nullable = false)
    private String contraseña;

    @Column(name = "UrlImagen")
    private String urlImagen;

    @Column(name = "PublicId")
    private String publicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private EstadoUsuario estado;

    @Column(name = "Ultimo_Acceso")
    private LocalDateTime ultimoAcceso;

    @CreationTimestamp
    @Column(name = "Fecha_Creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "Fecha_Modificacion")
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    public List<RolEntity> getRolesActivos() {
        return userRoles.stream()
                .filter(ur -> ur.getEstado() == EstadoUserRole.Activo)
                .map(UserRoleEntity::getRol)
                .filter(rol -> rol.getEstado() == EstadoRol.Activo)
                .toList();
    }

    public void addRole(RolEntity rol) {
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUsuario(this);
        userRole.setRol(rol);
        userRole.setEstado(EstadoUserRole.Activo);
        userRoles.add(userRole);
    }

    public void removeRole(RolEntity rol) {
        userRoles.removeIf(ur -> ur.getRol().getIdRol().equals(rol.getIdRol()));
    }
}