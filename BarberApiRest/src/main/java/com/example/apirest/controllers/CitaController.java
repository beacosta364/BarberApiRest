package com.example.apirest.controllers;

import com.example.apirest.controllers.dto.CitaDTO;
import com.example.apirest.persistence.entities.Cita;
import com.example.apirest.services.CitaService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    private CitaDTO convertirADTO(Cita cita) {

        return new CitaDTO(
                cita.getId(),
                cita.getFechaHora(),
                cita.getEstado(),
                cita.getCliente().getId(),
                cita.getBarbero().getId(),
                cita.getServicio().getId()
        );
    }

    @Operation(
            summary = "listar citas",
            description = "Lista las citas registradas"
    )
    @GetMapping
    public ResponseEntity<List<CitaDTO>> listarCitas() {

        List<CitaDTO> citas = citaService.listarCitas()
                .stream()
                .map(this::convertirADTO)
                .toList();

        return ResponseEntity.ok(citas);
    }

    @Operation(
            summary = "buscar cita",
            description = "Permite buscar registro de citas por ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> buscarCitaPorId(
            @PathVariable Long id) {

        return citaService.buscarCitaPorId(id)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registra cita",
            description = "Permite registrar cita"
    )
    @PostMapping
    public ResponseEntity<CitaDTO> guardarCita(
            @RequestBody CitaDTO citaDTO) {

        Cita cita = new Cita();

        cita.setFechaHora(citaDTO.getFechaHora());
        cita.setEstado(citaDTO.getEstado());

        Cita guardada = citaService.guardarCita(
                cita,
                citaDTO.getClienteId(),
                citaDTO.getBarberoId(),
                citaDTO.getServicioId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirADTO(guardada));
    }

    @Operation(
            summary = "Actualiza cita",
            description = "Permite actualizar registro de cita"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CitaDTO> actualizarCita(
            @PathVariable Long id,
            @RequestBody CitaDTO citaDTO) {

        try {
            Cita cita = new Cita();

            cita.setFechaHora(citaDTO.getFechaHora());
            cita.setEstado(citaDTO.getEstado());

            Cita actualizada = citaService.actualizarCita(
                    id,
                    cita,
                    citaDTO.getClienteId(),
                    citaDTO.getBarberoId(),
                    citaDTO.getServicioId()
            );

            return ResponseEntity.ok(convertirADTO(actualizada));

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Elimina cita por ID",
            description = "Permite eliminar registro de cita por ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(
            @PathVariable Long id) {

        if (citaService.buscarCitaPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        citaService.eliminarCita(id);

        return ResponseEntity.noContent().build();
    }
}
