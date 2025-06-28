package com.api.diversity.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.ClienteEntity;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

@Repository
public interface IClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    List<ClienteEntity> findByEstado(EstadoCliente estado);

    List<ClienteEntity> findByTipoCliente(TipoCliente tipoCliente);

    List<ClienteEntity> findByNombreCompletoContainingIgnoreCase(String nombreCompleto);

    List<ClienteEntity> findByEmailContainingIgnoreCase(String email);

    List<ClienteEntity> findByTelefonoContaining(String telefono);

    @Query("SELECT c FROM ClienteEntity c WHERE c.nombreCompleto LIKE %:termino% OR c.email LIKE %:termino% OR c.numeroDocumento LIKE %:termino%")
    List<ClienteEntity> buscarPorTermino(@Param("termino") String termino);

    List<ClienteEntity> findTop10ByOrderByIdClienteDesc();

    Long countByEstado(EstadoCliente estado);

    Long countByTipoCliente(TipoCliente tipoCliente);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    Optional<ClienteEntity> findByDni(String dni);
}