package com.api.diversity.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.UsuarioRubroEntity;

@Repository
public interface UsuarioRubroJpaRepository extends JpaRepository<UsuarioRubroEntity, Long> {

    @Query("SELECT ur FROM UsuarioRubroEntity ur WHERE ur.usuario.idUsuario = :usuarioId")
    List<UsuarioRubroEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT ur FROM UsuarioRubroEntity ur WHERE ur.rubro.idRubro = :rubroId")
    List<UsuarioRubroEntity> findByRubroId(@Param("rubroId") Long rubroId);

    @Query("SELECT ur FROM UsuarioRubroEntity ur WHERE ur.usuario.idUsuario = :usuarioId AND ur.estado = :estado")
    List<UsuarioRubroEntity> findByUsuarioIdAndEstado(@Param("usuarioId") Long usuarioId,
            @Param("estado") String estado);

    @Query("SELECT COUNT(ur) > 0 FROM UsuarioRubroEntity ur WHERE ur.usuario.idUsuario = :usuarioId AND ur.rubro.idRubro = :rubroId")
    boolean existsByUsuarioIdAndRubroId(@Param("usuarioId") Long usuarioId, @Param("rubroId") Long rubroId);
}