package com.example.apirest.persistence.repositories;

import com.example.apirest.persistence.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioJpaRepository extends JpaRepository<Servicio, Long> {

    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Long id);
}
