package com.smartradiology.project.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.ImagingSchedule;
import com.smartradiology.project.model.Patient;
import com.smartradiology.project.model.Report;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RadiologyRequestDTO {
    private Long id;
    private Doctor doctor;
    private Patient patient;
    private String imagingType;
    private LocalDateTime requestedAt;
    @JsonIgnore
    private ImagingSchedule imagingSchedule;
    private String reportImageUrl;
    private Report report;
    @JsonIgnore
    private boolean priority;
}
