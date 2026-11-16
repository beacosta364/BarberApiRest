package com.example.apirest.services;

import com.example.apirest.persistence.entities.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {
    boolean existeNombre(String nombre);
    boolean existeNombreEnOtroServicio(String nombre, Long id);
    List<Servicio> listarServicios();
    Optional<Servicio> buscarServicioPorId(Long id);
    Servicio guardarServicio(Servicio servicio);
    Servicio actualizarServicio(Long id, Servicio servicio);
    void eliminarServicio(Long id);
}
