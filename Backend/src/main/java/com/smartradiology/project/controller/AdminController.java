package com.smartradiology.project.controller;

import com.smartradiology.project.config.AppConstants;
import com.smartradiology.project.payload.DoctorDTO;
import com.smartradiology.project.payload.DoctorListResponse;
import com.smartradiology.project.payload.TechnicianDTO;
import com.smartradiology.project.payload.TechnicianListResponse;
import com.smartradiology.project.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/getDoctors")
    public ResponseEntity<DoctorListResponse> getAllDoctors(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_DOCTORS_BY)String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)String sortOrder
    ){
        DoctorListResponse doctorListResponse = adminService.getAllDoctors(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<DoctorListResponse>(doctorListResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/getTechnicians")
    public ResponseEntity<TechnicianListResponse> getAllTechnicians(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_TECHNICIANS_BY)String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIRECTION)String sortOrder

    ){
        TechnicianListResponse technicianListResponse = adminService.getAllTechnician(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<TechnicianListResponse>(technicianListResponse, HttpStatus.OK);

    }

    @PostMapping("/admin/createDoctor")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        DoctorDTO doctorDTO1 = adminService.createDoctor(doctorDTO);
        return new ResponseEntity<DoctorDTO>(doctorDTO1, HttpStatus.OK);
    }

    @PostMapping("/admin/createTechnician")
    public ResponseEntity<TechnicianDTO> createTechnician(@RequestBody TechnicianDTO technicianDTO){
        TechnicianDTO technicianDTO1 = adminService.createTechnician(technicianDTO);
        return new ResponseEntity<TechnicianDTO>(technicianDTO1, HttpStatus.OK);
    }
}
