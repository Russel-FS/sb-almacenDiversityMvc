package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ProveedorEntity;
import com.api.diversity.domain.enums.EstadoProveedor;

@Repository
public interface IProveedorJpaRepository extends JpaRepository<ProveedorEntity, Long> {

    List<ProveedorEntity> findByEstado(EstadoProveedor estado);

    Optional<ProveedorEntity> findByNumeroDocumento(String numeroDocumento);

    List<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String razonSocial);

    List<ProveedorEntity> findByRepresentanteLegalContainingIgnoreCase(String representanteLegal);

    List<ProveedorEntity> findByEmailContainingIgnoreCase(String email);

    List<ProveedorEntity> findByTelefonoContaining(String telefono);

    @Query("SELECT p FROM ProveedorEntity p WHERE p.razonSocial LIKE %:termino% OR p.representanteLegal LIKE %:termino% OR p.email LIKE %:termino% OR p.ruc LIKE %:termino%")
    List<ProveedorEntity> buscarPorTermino(@Param("termino") String termino);

    List<ProveedorEntity> findTop10ByOrderByIdProveedorDesc();

    Long countByEstado(EstadoProveedor estado);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByEmail(String email);
}