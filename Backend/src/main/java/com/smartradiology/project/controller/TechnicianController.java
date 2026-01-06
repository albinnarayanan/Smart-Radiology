package com.smartradiology.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartradiology.project.config.AppConstants;
import com.smartradiology.project.model.Report;
import com.smartradiology.project.payload.ImagingScheduleResponse;
import com.smartradiology.project.payload.RadiologyRequestResponse;
import com.smartradiology.project.payload.ReportDTO;
import com.smartradiology.project.service.TechnicianService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TechnicianController {



    @Autowired
    private TechnicianService technicianService;

    @GetMapping("/technician/getAllRequests")
    public ResponseEntity<ImagingScheduleResponse> getAllRequestsForTechnician(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_RADIOLOGY_REQUEST_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIRECTION) String sortOrder
    ){
        ImagingScheduleResponse imagingScheduleResponse = technicianService.getAllRequests(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<ImagingScheduleResponse>(imagingScheduleResponse, HttpStatus.OK);
    }

    @PostMapping("technician/report/{imagingScheduleId}/image")
    public ResponseEntity<ReportDTO> postReport(
            @RequestPart("report") MultipartFile reportPart,
            @PathVariable Long imagingScheduleId,
            @RequestPart("image")MultipartFile image

            ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReportDTO reportDTO = objectMapper.readValue(reportPart.getInputStream(), ReportDTO.class);

        ReportDTO savedReport =  technicianService.submitReport(imagingScheduleId,reportDTO, image);
        return new ResponseEntity<ReportDTO>(savedReport,HttpStatus.OK);
    }
}
