package com.smartradiology.project.repository;

import com.smartradiology.project.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByUsername(String username);

    Boolean existsByEmail(String email);
}
