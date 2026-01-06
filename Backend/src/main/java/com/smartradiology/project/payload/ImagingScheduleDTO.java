package com.smartradiology.project.payload;

import com.smartradiology.project.model.RadiologyRequest;
import com.smartradiology.project.model.Technician;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagingScheduleDTO {
    private Long id;
    private LocalDateTime scheduledTime;
    private Technician technician;
    private RadiologyRequest radiologyRequest;
}
