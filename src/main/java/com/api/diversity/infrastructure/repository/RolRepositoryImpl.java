package com.api.diversity.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.RolEntity;
import com.api.diversity.domain.ports.IRolRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RolRepositoryImpl implements IRolRepository {

    private final IRolJpaRepository rolJpaRepository;

    @Override
    public Optional<RolEntity> findById(Long id) {
        return rolJpaRepository.findById(id);
    }

    @Override
    public RolEntity save(RolEntity rol) {
        return rolJpaRepository.save(rol);
    }
}