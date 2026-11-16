package com.example.apirest.services;

import com.example.apirest.persistence.entities.Barbero;
import com.example.apirest.persistence.repositories.BarberoJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarberoServiceImpl implements BarberoService {

    private final BarberoJpaRepository barberoRepository;

    public BarberoServiceImpl(BarberoJpaRepository barberoRepository) {
        this.barberoRepository = barberoRepository;
    }

    @Override
    public List<Barbero> listarBarberos() {
        return barberoRepository.findAll();
    }

    @Override
    public Optional<Barbero> buscarBarberoPorId(Long id) {
        return barberoRepository.findById(id);
    }

    @Override
    public Barbero guardarBarbero(Barbero barbero) {
        return barberoRepository.save(barbero);
    }

    @Override
    public Barbero actualizarBarbero(Long id, Barbero barbero) {
        Barbero barberoExistente = barberoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));

        barberoExistente.setNombre(barbero.getNombre());
        barberoExistente.setTelefono(barbero.getTelefono());
        barberoExistente.setEspecialidad(barbero.getEspecialidad());

        return barberoRepository.save(barberoExistente);
    }

    @Override
    public void eliminarBarbero(Long id) {
        barberoRepository.deleteById(id);
    }

    @Override
    public boolean existeTelefono(String telefono) {
        return barberoRepository.existsByTelefono(telefono);
    }

    @Override
    public boolean existeTelefonoEnOtroBarbero(String telefono, Long id) {
        return barberoRepository.existsByTelefonoAndIdNot(telefono, id);
    }
}
