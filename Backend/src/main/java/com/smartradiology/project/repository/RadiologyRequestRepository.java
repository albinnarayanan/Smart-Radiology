package com.smartradiology.project.repository;

import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.ImagingSchedule;
import com.smartradiology.project.model.RadiologyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RadiologyRequestRepository extends JpaRepository<RadiologyRequest, Long> {
    Optional<RadiologyRequest> findByImagingSchedule(ImagingSchedule imagingSchedule);

    Page<RadiologyRequest> findByDoctor(Doctor doctor, Pageable pageDetails);
}
