/*package com.example.AVANCE.services;

//importamos la libreria de coleccion de datos
import java.util.List;


//importamos las anotaciones y servicios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//importamos el model entrada y el repository
import com.example.AVANCE.model.Entrada;
import com.example.AVANCE.repository.EntradaRepository;

//declaramos la clase como un servicio
@Service
public class EntradaServiceImpl implements EntradaService {

     @Autowired
    private EntradaRepository entradaRepository;

    @Override
    public List<Entrada> listar() {
        return entradaRepository.findAll();
    }

    @Override
    public Entrada buscarPorId(Long id) {
        return entradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la entrada con ID: " + id));
    }

    @Override
    public Entrada registrar(Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    @Override
    public void eliminar(Long id) {
        entradaRepository.deleteById(id);
    }

    @Override
    public Entrada actualizar(Long id, Entrada entradaActualizada) {
        Entrada entrada = buscarPorId(id);
        entrada.setNumeroFactura(entradaActualizada.getNumeroFactura());
        entrada.setIdProveedor(entradaActualizada.getIdProveedor());
        entrada.setFechaEntrada(entradaActualizada.getFechaEntrada());
        entrada.setCostoTotal(entradaActualizada.getCostoTotal());
        entrada.setEstado(entradaActualizada.getEstado());
        entrada.setIdUsuarioRegistro(entradaActualizada.getIdUsuarioRegistro());
        entrada.setIdUsuarioAprobacion(entradaActualizada.getIdUsuarioAprobacion());
        entrada.setFechaAprobacion(entradaActualizada.getFechaAprobacion());
        entrada.setObservaciones(entradaActualizada.getObservaciones());
        return entradaRepository.save(entrada);
    }
}*/
