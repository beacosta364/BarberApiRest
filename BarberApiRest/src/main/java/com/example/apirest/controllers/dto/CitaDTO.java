package com.example.apirest.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDTO {

    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy-HH:mm:ss")
    private LocalDateTime fechaHora;
    private String estado;
    private Long clienteId;
    private Long barberoId;
    private Long servicioId;
}
