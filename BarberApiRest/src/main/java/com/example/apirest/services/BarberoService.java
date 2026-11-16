package com.example.apirest.services;

import com.example.apirest.persistence.entities.Barbero;

import java.util.List;
import java.util.Optional;

public interface BarberoService {
    boolean existeTelefono(String telefono);
    boolean existeTelefonoEnOtroBarbero(String telefono, Long id);
    List<Barbero> listarBarberos();
    Optional<Barbero> buscarBarberoPorId(Long id);
    Barbero guardarBarbero(Barbero barbero);
    Barbero actualizarBarbero(Long id, Barbero barbero);
    void eliminarBarbero(Long id);
}
