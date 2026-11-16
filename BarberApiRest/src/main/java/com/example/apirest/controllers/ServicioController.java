package com.example.apirest.controllers;

import com.example.apirest.controllers.dto.ServicioDTO;
import com.example.apirest.persistence.entities.Servicio;
import com.example.apirest.services.ServicioService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @Operation(
            summary = "Listar servicios",
            description = "Lista los servicios registrados"
    )
    @GetMapping
    public ResponseEntity<List<ServicioDTO>> listarServicios() {

        List<ServicioDTO> servicios = servicioService.listarServicios()
                .stream()
                .map(servicio -> new ServicioDTO(
                        servicio.getId(),
                        servicio.getNombre(),
                        servicio.getDescripcion(),
                        servicio.getPrecio()
                ))
                .toList();

        return ResponseEntity.ok(servicios);
    }

    @Operation(
            summary = "Buscar servicio por ID",
            description = "Permite buscar un registro de servicio"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> buscarServicioPorId(
            @PathVariable Long id) {

        return servicioService.buscarServicioPorId(id)
                .map(servicio -> new ServicioDTO(
                        servicio.getId(),
                        servicio.getNombre(),
                        servicio.getDescripcion(),
                        servicio.getPrecio()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar servicio",
            description = "Permite registrar nuevos servicios"
    )
    @PostMapping
    public ResponseEntity<?> guardarServicio(
            @RequestBody ServicioDTO servicioDTO) {

        if (servicioService.existeNombre(servicioDTO.getNombre())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El nombre del servicio ya se encuentra registrado.");
        }

        Servicio servicio = new Servicio(
                null,
                servicioDTO.getNombre(),
                servicioDTO.getDescripcion(),
                servicioDTO.getPrecio()
        );

        Servicio guardado = servicioService.guardarServicio(servicio);

        ServicioDTO respuesta = new ServicioDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getDescripcion(),
                guardado.getPrecio()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Operation(
            summary = "Actualizar servicio",
            description = "Permite actualizar un registro de servicio"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarServicio(
            @PathVariable Long id,
            @RequestBody ServicioDTO servicioDTO) {

        if (servicioService.existeNombreEnOtroServicio(
                servicioDTO.getNombre(), id)) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El nombre del servicio ya está asociado a otro servicio.");
        }

        try {
            Servicio servicio = new Servicio(
                    id,
                    servicioDTO.getNombre(),
                    servicioDTO.getDescripcion(),
                    servicioDTO.getPrecio()
            );

            Servicio actualizado = servicioService.actualizarServicio(id, servicio);

            ServicioDTO respuesta = new ServicioDTO(
                    actualizado.getId(),
                    actualizado.getNombre(),
                    actualizado.getDescripcion(),
                    actualizado.getPrecio()
            );

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró el servicio que intenta actualizar.");
        }
    }

    @Operation(
            summary = "Eliminar servicio por ID",
            description = "Permite eliminar un registro de servicio"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(
            @PathVariable Long id) {

        if (servicioService.buscarServicioPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        servicioService.eliminarServicio(id);

        return ResponseEntity.noContent().build();
    }
}

