package com.example.apirest.services;

import com.example.apirest.persistence.entities.Servicio;
import com.example.apirest.persistence.repositories.ServicioJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioJpaRepository servicioRepository;

    public ServicioServiceImpl(ServicioJpaRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    @Override
    public Optional<Servicio> buscarServicioPorId(Long id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Servicio guardarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public Servicio actualizarServicio(Long id, Servicio servicio) {
        Servicio servicioExistente = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicioExistente.setNombre(servicio.getNombre());
        servicioExistente.setDescripcion(servicio.getDescripcion());
        servicioExistente.setPrecio(servicio.getPrecio());

        return servicioRepository.save(servicioExistente);
    }

    @Override
    public void eliminarServicio(Long id) {
        servicioRepository.deleteById(id);
    }

    @Override
    public boolean existeNombre(String nombre) {
        return servicioRepository.existsByNombre(nombre);
    }

    @Override
    public boolean existeNombreEnOtroServicio(String nombre, Long id) {
        return servicioRepository.existsByNombreAndIdNot(nombre, id);
    }
}
