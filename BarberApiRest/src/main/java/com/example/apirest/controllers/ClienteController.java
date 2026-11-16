package com.example.apirest.controllers;

import com.example.apirest.controllers.dto.ClienteDTO;
import com.example.apirest.persistence.entities.Cliente;
import com.example.apirest.services.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Listar clientes",
            description = "Lista los clientes registrados"
    )
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {

        List<ClienteDTO> clientes = clienteService.listarClientes()
                .stream()
                .map(cliente -> new ClienteDTO(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getCorreo()
                ))
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Permite buscar un registro de cliente"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(
            @PathVariable Long id) {

        return clienteService.buscarClientePorId(id)
                .map(cliente -> new ClienteDTO(
                        cliente.getId(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getCorreo()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar cliente",
            description = "Permite registrar nuevos clientes"
    )
    @PostMapping
    public ResponseEntity<?> guardarCliente(
            @RequestBody ClienteDTO clienteDTO) {

        // VALIDAR CORREO DUPLICADO
        if (clienteService.existeCorreo(clienteDTO.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El correo electrónico ya se encuentra registrado.");
        }

        Cliente cliente = new Cliente(
                null,
                clienteDTO.getNombre(),
                clienteDTO.getTelefono(),
                clienteDTO.getCorreo()
        );

        Cliente guardado = clienteService.guardarCliente(cliente);

        ClienteDTO respuesta = new ClienteDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getTelefono(),
                guardado.getCorreo()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Operation(
            summary = "Actualizar cliente",
            description = "Permite actualizar un registro de cliente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO clienteDTO) {

        if (clienteService.existeCorreoEnOtroCliente(
                clienteDTO.getCorreo(), id)) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El correo electrónico ya está asociado a otro cliente.");
        }

        try {
            Cliente cliente = new Cliente(
                    id,
                    clienteDTO.getNombre(),
                    clienteDTO.getTelefono(),
                    clienteDTO.getCorreo()
            );

            Cliente actualizado = clienteService.actualizarCliente(id, cliente);

            ClienteDTO respuesta = new ClienteDTO(
                    actualizado.getId(),
                    actualizado.getNombre(),
                    actualizado.getTelefono(),
                    actualizado.getCorreo()
            );

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró el cliente que intenta actualizar.");
        }
    }

    @Operation(
            summary = "Eliminar cliente por ID",
            description = "Permite eliminar un registro de cliente"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @PathVariable Long id) {

        if (clienteService.buscarClientePorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        clienteService.eliminarCliente(id);

        return ResponseEntity.noContent().build();
    }
}
