package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.enums.EstadoUserRole;
import com.api.diversity.domain.model.UserRoleEntity;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findByUsuarioIdUsuario(Long usuarioId);

    List<UserRoleEntity> findByRolIdRol(Long rolId);

    Optional<UserRoleEntity> findByUsuarioIdUsuarioAndRolIdRol(Long usuarioId, Long rolId);

    List<UserRoleEntity> findByUsuarioIdUsuarioAndEstado(Long usuarioId, EstadoUserRole estado);

    @Query("SELECT ur FROM UserRoleEntity ur WHERE ur.usuario.idUsuario = :usuarioId AND ur.estado = :estado")
    List<UserRoleEntity> findActiveRolesByUsuario(@Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoUserRole estado);

    boolean existsByUsuarioIdUsuarioAndRolIdRol(Long usuarioId, Long rolId);

    void deleteByUsuarioIdUsuarioAndRolIdRol(Long usuarioId, Long rolId);
}