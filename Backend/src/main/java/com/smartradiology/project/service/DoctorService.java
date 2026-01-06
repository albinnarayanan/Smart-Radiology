package com.smartradiology.project.service;

import com.smartradiology.project.payload.PatientDTO;
import com.smartradiology.project.payload.PatientListResponse;
import com.smartradiology.project.payload.RadiologyRequestDTO;
import com.smartradiology.project.payload.RadiologyRequestResponse;

public interface DoctorService {
    PatientDTO addPatient(PatientDTO patientDTO);

    RadiologyRequestDTO createRadiologyRequest(Long patientId, String imagingType);

    RadiologyRequestResponse getAllRequests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PatientListResponse getAllPatients(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);
}
