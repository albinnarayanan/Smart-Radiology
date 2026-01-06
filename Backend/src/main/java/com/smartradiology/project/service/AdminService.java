package com.smartradiology.project.service;


import com.smartradiology.project.payload.DoctorDTO;
import com.smartradiology.project.payload.DoctorListResponse;
import com.smartradiology.project.payload.TechnicianDTO;
import com.smartradiology.project.payload.TechnicianListResponse;

public interface AdminService {
    DoctorListResponse getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    TechnicianListResponse getAllTechnician(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    TechnicianDTO createTechnician(TechnicianDTO technicianDTO);
}
