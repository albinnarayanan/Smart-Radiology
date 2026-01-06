package com.smartradiology.project.controller;

import com.smartradiology.project.config.AppConstants;
import com.smartradiology.project.payload.PatientDTO;
import com.smartradiology.project.payload.PatientListResponse;
import com.smartradiology.project.payload.RadiologyRequestDTO;
import com.smartradiology.project.payload.RadiologyRequestResponse;
import com.smartradiology.project.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;


    @PostMapping("/doctor/createPatient")
    public ResponseEntity<PatientDTO>createPatient(
            @RequestBody PatientDTO patientDTO){

        PatientDTO savedPatientDto = doctorService.addPatient(patientDTO);
        return new ResponseEntity<PatientDTO>(savedPatientDto,HttpStatus.CREATED);

    }


    @PostMapping("/doctor/createRadiologyRequest/{patientId}/imagingType/{imagingType}")
    public ResponseEntity<RadiologyRequestDTO> createRadiologyRequest(
            @PathVariable Long patientId,
            @PathVariable String imagingType
            ){
        RadiologyRequestDTO savedRequestResponse = doctorService.createRadiologyRequest(patientId, imagingType);
        return new ResponseEntity<>(savedRequestResponse, HttpStatus.CREATED);


    }
    @GetMapping("/doctor/getAllRadiologyReq")
    public ResponseEntity<RadiologyRequestResponse> getAllRadiologyRequests(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_RADIOLOGY_REQUEST_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder
    ){
        RadiologyRequestResponse response = doctorService.getAllRequests(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<RadiologyRequestResponse>(response,HttpStatus.OK);

    }

    @GetMapping("/doctor/getAllPatients")
    public ResponseEntity<PatientListResponse> getAllPatients(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PATIENTS_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder
    ){
        PatientListResponse patientList = doctorService.getAllPatients(pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<PatientListResponse>(patientList,HttpStatus.OK);
    }


}
