package com.example.apirest.controllers;

import com.example.apirest.controllers.dto.BarberoDTO;
import com.example.apirest.persistence.entities.Barbero;
import com.example.apirest.services.BarberoService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barberos")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @Operation(
            summary = "Listar barberos",
            description = "Lista los barberos registrados"
    )
    @GetMapping
    public ResponseEntity<List<BarberoDTO>> listarBarberos() {

        List<BarberoDTO> barberos = barberoService.listarBarberos()
                .stream()
                .map(barbero -> new BarberoDTO(
                        barbero.getId(),
                        barbero.getNombre(),
                        barbero.getTelefono(),
                        barbero.getEspecialidad()
                ))
                .toList();

        return ResponseEntity.ok(barberos);
    }

    @Operation(
            summary = "Buscar barbero por ID",
            description = "Permite buscar un registro de barbero"
    )
    @GetMapping("/{id}")
    public ResponseEntity<BarberoDTO> buscarBarberoPorId(
            @PathVariable Long id) {

        return barberoService.buscarBarberoPorId(id)
                .map(barbero -> new BarberoDTO(
                        barbero.getId(),
                        barbero.getNombre(),
                        barbero.getTelefono(),
                        barbero.getEspecialidad()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar barbero",
            description = "Permite registrar nuevos barberos"
    )
    @PostMapping
    public ResponseEntity<?> guardarBarbero(
            @RequestBody BarberoDTO barberoDTO) {

        if (barberoService.existeTelefono(barberoDTO.getTelefono())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El número de teléfono ya se encuentra registrado.");
        }

        Barbero barbero = new Barbero(
                null,
                barberoDTO.getNombre(),
                barberoDTO.getTelefono(),
                barberoDTO.getEspecialidad()
        );

        Barbero guardado = barberoService.guardarBarbero(barbero);

        BarberoDTO respuesta = new BarberoDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getTelefono(),
                guardado.getEspecialidad()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Operation(
            summary = "Actualizar barbero",
            description = "Permite actualizar un registro de barbero"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarBarbero(
            @PathVariable Long id,
            @RequestBody BarberoDTO barberoDTO) {

        if (barberoService.existeTelefonoEnOtroBarbero(
                barberoDTO.getTelefono(), id)) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El número de teléfono ya está asociado a otro barbero.");
        }

        try {
            Barbero barbero = new Barbero(
                    id,
                    barberoDTO.getNombre(),
                    barberoDTO.getTelefono(),
                    barberoDTO.getEspecialidad()
            );

            Barbero actualizado = barberoService.actualizarBarbero(id, barbero);

            BarberoDTO respuesta = new BarberoDTO(
                    actualizado.getId(),
                    actualizado.getNombre(),
                    actualizado.getTelefono(),
                    actualizado.getEspecialidad()
            );

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró el barbero que intenta actualizar.");
        }
    }

    @Operation(
            summary = "Eliminar barbero por ID",
            description = "Permite eliminar un registro de barbero"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBarbero(
            @PathVariable Long id) {

        if (barberoService.buscarBarberoPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        barberoService.eliminarBarbero(id);

        return ResponseEntity.noContent().build();
    }
}
