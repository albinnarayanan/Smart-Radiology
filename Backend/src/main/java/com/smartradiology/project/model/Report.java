package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    private LocalDateTime generatedAt;
    @NotBlank
    @Lob
    private String findings;

    private String image;

    @OneToOne
    @JoinColumn(name = "radiology_request_id")
    @JsonBackReference("report-radiology-request")
    private RadiologyRequest radiologyRequest;
}
