package com.api.diversity.domain.ports;

import java.util.List;
import java.util.Optional;

import com.api.diversity.domain.model.UserRoleEntity;

public interface IUserRoleRepository {

    UserRoleEntity save(UserRoleEntity userRole);

    Optional<UserRoleEntity> findById(Long id);

    List<UserRoleEntity> findByUsuarioId(Long usuarioId);

    List<UserRoleEntity> findByRolId(Long rolId);

    Optional<UserRoleEntity> findByUsuarioIdAndRolId(Long usuarioId, Long rolId);

    List<UserRoleEntity> findByUsuarioIdAndEstado(Long usuarioId, com.api.diversity.domain.enums.EstadoUserRole estado);

    void deleteById(Long id);

    void deleteByUsuarioIdAndRolId(Long usuarioId, Long rolId);

    boolean existsByUsuarioIdAndRolId(Long usuarioId, Long rolId);

    List<UserRoleEntity> findAll();
}