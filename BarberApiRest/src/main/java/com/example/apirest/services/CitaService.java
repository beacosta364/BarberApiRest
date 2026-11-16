package com.example.apirest.services;

import com.example.apirest.persistence.entities.Cita;

import java.util.List;
import java.util.Optional;

public interface CitaService {

    List<Cita> listarCitas();
    Optional<Cita> buscarCitaPorId(Long id);
    Cita guardarCita(Cita cita, Long clienteId, Long barberoId, Long servicioId);
    Cita actualizarCita(Long id, Cita cita, Long clienteId, Long barberoId, Long servicioId);
    void eliminarCita(Long id);
}
