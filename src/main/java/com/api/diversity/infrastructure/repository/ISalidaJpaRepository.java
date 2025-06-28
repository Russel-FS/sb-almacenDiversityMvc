package com.api.diversity.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.SalidaEntity;
import com.api.diversity.domain.enums.EstadoSalida;

@Repository
public interface ISalidaJpaRepository extends JpaRepository<SalidaEntity, Long> {

    List<SalidaEntity> findByEstado(EstadoSalida estado);

    List<SalidaEntity> findByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<SalidaEntity> findByClienteId(Long clienteId);

    List<SalidaEntity> findByUsuarioRegistroId(Long usuarioId);

    @Query("SELECT s FROM SalidaEntity s JOIN s.detalles d JOIN d.producto p JOIN p.categoria c JOIN c.rubro r WHERE r.idRubro = :rubroId")
    List<SalidaEntity> findByRubroId(@Param("rubroId") Long rubroId);

    List<SalidaEntity> findTop10ByOrderByFechaSalidaDesc();

    Long countByEstado(EstadoSalida estado);

    Long countByFechaSalidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}