package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imaging_schedule")
public class ImagingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imaging_schedule_id")
    private Long imagingScheduleId;

    private LocalDateTime scheduledTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    @JsonBackReference("schedule-technician")
    private Technician technician;

    @OneToOne
    @JoinColumn(name = "radiology_request_id",unique = true)
    @JsonBackReference("schedule-radiology-request")
    private RadiologyRequest radiologyRequest;
}
