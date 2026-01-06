package com.smartradiology.project.repository;

import com.smartradiology.project.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByContactNumber(String contactNumber);
}
