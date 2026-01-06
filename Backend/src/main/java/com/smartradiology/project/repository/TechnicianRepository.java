package com.smartradiology.project.repository;

import com.smartradiology.project.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Optional<Technician> findByUsername(String username);

    Boolean existsByEmail(String email);
}
