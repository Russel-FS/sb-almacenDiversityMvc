package com.api.diversity.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.diversity.domain.model.EntradaEntity;
import com.api.diversity.domain.enums.EstadoEntrada;
import com.api.diversity.domain.enums.TipoEntrada;

@Repository
public interface IEntradaJpaRepository extends JpaRepository<EntradaEntity, Long> {

    List<EntradaEntity> findByEstado(EstadoEntrada estado);

    List<EntradaEntity> findByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<EntradaEntity> findByProveedorIdProveedor(Long idProveedor);

    List<EntradaEntity> findByUsuarioRegistroIdUsuario(Long idUsuario);

    @Query("SELECT e FROM EntradaEntity e JOIN e.detalles d JOIN d.producto p JOIN p.categoria c JOIN c.rubro r WHERE r.idRubro = :rubroId")
    List<EntradaEntity> findByRubroId(@Param("rubroId") Long rubroId);

    List<EntradaEntity> findTop10ByOrderByFechaEntradaDesc();

    Long countByEstado(EstadoEntrada estado);

    Long countByFechaEntradaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    boolean existsByNumeroFactura(String numeroFactura);

    @Query("SELECT e FROM EntradaEntity e JOIN e.detalles d JOIN d.producto p JOIN p.categoria c JOIN c.rubro r WHERE r.idRubro = :rubroId AND e.estado = :estado")
    List<EntradaEntity> findByRubroIdAndEstado(@Param("rubroId") Long rubroId, @Param("estado") EstadoEntrada estado);

    // Nuevo método para encontrar entradas de devolución por ID de salida de referencia
    List<EntradaEntity> findByTipoEntradaAndIdSalidaReferencia(TipoEntrada tipoEntrada, Long idSalidaReferencia);
}
