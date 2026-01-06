package com.smartradiology.project.repository;

import com.smartradiology.project.model.ImagingSchedule;
import com.smartradiology.project.model.RadiologyRequest;
import com.smartradiology.project.model.Technician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImagingScheduleRepository extends JpaRepository<ImagingSchedule,Long> {

    Optional<ImagingSchedule> findByRadiologyRequest(RadiologyRequest radiologyRequest);

    Page<ImagingSchedule> findByTechnician(Technician technician, Pageable pageDetails);
}
