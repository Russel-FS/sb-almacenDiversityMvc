package com.api.diversity.application.service.interfaces;

import java.util.List;

import com.api.diversity.application.dto.ClienteDto;
import com.api.diversity.domain.enums.EstadoCliente;
import com.api.diversity.domain.enums.TipoCliente;

public interface IClienteService {

    ClienteDto save(ClienteDto clienteDto);

    ClienteDto update(ClienteDto clienteDto);

    void deleteById(Long id);

    ClienteDto cambiarEstado(Long id, EstadoCliente nuevoEstado);

    ClienteDto findById(Long id);

    ClienteDto findByDni(String dni);

    ClienteDto findByEmail(String email);

    List<ClienteDto> findAll();

    List<ClienteDto> findByEstado(EstadoCliente estado);

    List<ClienteDto> findByTipoCliente(TipoCliente tipoCliente);

    List<ClienteDto> findByNombreCompletoContainingIgnoreCase(String nombre);

    List<ClienteDto> findTop10ByOrderByFechaRegistroDesc();

    boolean existsById(Long id);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    Long countByEstado(EstadoCliente estado);

    Long countByTipoCliente(TipoCliente tipoCliente);

    Long countTotal();
}