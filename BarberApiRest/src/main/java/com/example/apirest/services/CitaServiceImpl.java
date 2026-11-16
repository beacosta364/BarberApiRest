package com.example.apirest.services;

import com.example.apirest.persistence.entities.Cita;
import com.example.apirest.persistence.entities.Cliente;
import com.example.apirest.persistence.entities.Barbero;
import com.example.apirest.persistence.entities.Servicio;

import com.example.apirest.persistence.repositories.CitaJpaRepository;
import com.example.apirest.persistence.repositories.ClienteJpaRepository;
import com.example.apirest.persistence.repositories.BarberoJpaRepository;
import com.example.apirest.persistence.repositories.ServicioJpaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaJpaRepository citaRepository;
    private final ClienteJpaRepository clienteRepository;
    private final BarberoJpaRepository barberoRepository;
    private final ServicioJpaRepository servicioRepository;

    public CitaServiceImpl(
            CitaJpaRepository citaRepository,
            ClienteJpaRepository clienteRepository,
            BarberoJpaRepository barberoRepository,
            ServicioJpaRepository servicioRepository) {

        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.barberoRepository = barberoRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> buscarCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    public Cita guardarCita(
            Cita cita,
            Long clienteId,
            Long barberoId,
            Long servicioId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Barbero barbero = barberoRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);

        return citaRepository.save(cita);
    }

    @Override
    public Cita actualizarCita(
            Long id,
            Cita cita,
            Long clienteId,
            Long barberoId,
            Long servicioId) {

        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Barbero barbero = barberoRepository.findById(barberoId)
                .orElseThrow(() -> new RuntimeException("Barbero no encontrado"));

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        citaExistente.setFechaHora(cita.getFechaHora());
        citaExistente.setEstado(cita.getEstado());
        citaExistente.setCliente(cliente);
        citaExistente.setBarbero(barbero);
        citaExistente.setServicio(servicio);

        return citaRepository.save(citaExistente);
    }

    @Override
    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }
}
