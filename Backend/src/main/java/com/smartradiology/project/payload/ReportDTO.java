package com.smartradiology.project.payload;

import com.smartradiology.project.model.RadiologyRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
    private Long id;
    private LocalDateTime generatedAt;
    private String findings;
    private String image;
    private RadiologyRequest radiologyRequest;
}
