package com.example.apirest.persistence.repositories;

import com.example.apirest.persistence.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaJpaRepository extends JpaRepository<Cita, Long> {

}
