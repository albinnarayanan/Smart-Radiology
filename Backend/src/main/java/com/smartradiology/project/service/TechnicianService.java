package com.smartradiology.project.service;

import com.smartradiology.project.payload.ImagingScheduleResponse;
import com.smartradiology.project.payload.RadiologyRequestResponse;
import com.smartradiology.project.payload.ReportDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TechnicianService {
    ImagingScheduleResponse getAllRequests(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ReportDTO submitReport(Long imagingScheduleId, ReportDTO reportDTO, MultipartFile image) throws IOException;
}
