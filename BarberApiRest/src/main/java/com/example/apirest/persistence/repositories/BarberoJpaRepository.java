package com.example.apirest.persistence.repositories;

import com.example.apirest.persistence.entities.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarberoJpaRepository extends JpaRepository<Barbero, Long> {

    boolean existsByTelefono(String telefono);
    boolean existsByTelefonoAndIdNot(String telefono, Long id);
}
