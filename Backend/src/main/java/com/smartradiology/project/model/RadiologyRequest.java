package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "radiology_request")
public class RadiologyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "radiology_request_id")
    private Long radiologyRequestId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank
    private String imagingType;

    private LocalDateTime requestedAt;

    @OneToOne(mappedBy = "radiologyRequest", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("schedule-radiology-request")
    private ImagingSchedule imagingSchedule;

    @OneToOne(mappedBy = "radiologyRequest",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference("report-radiology-request")
    private Report report;

    private boolean priority;

}
