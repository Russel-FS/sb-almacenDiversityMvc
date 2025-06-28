package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.enums.EstadoUserRole;
import com.api.diversity.domain.model.UserRoleEntity;
import com.api.diversity.domain.ports.IUserRoleRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements IUserRoleRepository {

    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public UserRoleEntity save(UserRoleEntity userRole) {
        return userRoleJpaRepository.save(userRole);
    }

    @Override
    public Optional<UserRoleEntity> findById(Long id) {
        return userRoleJpaRepository.findById(id);
    }

    @Override
    public List<UserRoleEntity> findByUsuarioId(Long usuarioId) {
        return userRoleJpaRepository.findByUsuarioIdUsuario(usuarioId);
    }

    @Override
    public List<UserRoleEntity> findByRolId(Long rolId) {
        return userRoleJpaRepository.findByRolIdRol(rolId);
    }

    @Override
    public Optional<UserRoleEntity> findByUsuarioIdAndRolId(Long usuarioId, Long rolId) {
        return userRoleJpaRepository.findByUsuarioIdUsuarioAndRolIdRol(usuarioId, rolId);
    }

    @Override
    public List<UserRoleEntity> findByUsuarioIdAndEstado(Long usuarioId, EstadoUserRole estado) {
        return userRoleJpaRepository.findByUsuarioIdUsuarioAndEstado(usuarioId, estado);
    }

    @Override
    public void deleteById(Long id) {
        userRoleJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUsuarioIdAndRolId(Long usuarioId, Long rolId) {
        userRoleJpaRepository.deleteByUsuarioIdUsuarioAndRolIdRol(usuarioId, rolId);
    }

    @Override
    public boolean existsByUsuarioIdAndRolId(Long usuarioId, Long rolId) {
        return userRoleJpaRepository.existsByUsuarioIdUsuarioAndRolIdRol(usuarioId, rolId);
    }

    @Override
    public List<UserRoleEntity> findAll() {
        return userRoleJpaRepository.findAll();
    }
}