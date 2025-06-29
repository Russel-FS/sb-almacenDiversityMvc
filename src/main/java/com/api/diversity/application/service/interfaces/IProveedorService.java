package com.api.diversity.application.service.interfaces;

import java.util.List;

import com.api.diversity.application.dto.ProveedorDto;
import com.api.diversity.domain.enums.EstadoProveedor;

public interface IProveedorService {

    ProveedorDto save(ProveedorDto proveedorDto);

    ProveedorDto update(ProveedorDto proveedorDto);

    void deleteById(Long id);

    ProveedorDto cambiarEstado(Long id, EstadoProveedor nuevoEstado);

    ProveedorDto findById(Long id);

    ProveedorDto findByRuc(String ruc);

    ProveedorDto findByEmail(String email);

    List<ProveedorDto> findAll();

    List<ProveedorDto> findByEstado(EstadoProveedor estado);

    List<ProveedorDto> findByRazonSocialContainingIgnoreCase(String razonSocial);

    List<ProveedorDto> findByRepresentanteLegalContainingIgnoreCase(String representanteLegal);

    List<ProveedorDto> findTop10ByOrderByFechaRegistroDesc();

    boolean existsById(Long id);

    boolean existsByRuc(String ruc);

    boolean existsByEmail(String email);

    Long countByEstado(EstadoProveedor estado);

    Long countTotal();
}