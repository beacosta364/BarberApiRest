package com.example.apirest.services;

import com.example.apirest.persistence.entities.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    boolean existeCorreo(String correo);
    boolean existeCorreoEnOtroCliente(String correo, Long id);
    List<Cliente> listarClientes();
    Optional<Cliente> buscarClientePorId(Long id);
    Cliente guardarCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
}


