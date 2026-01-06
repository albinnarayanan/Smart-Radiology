package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "technician")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "technician_id")
    private Long technicianId;

    @Size(max = 30)
    private String name;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;


    @OneToMany(mappedBy = "technician",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JsonManagedReference("schedule-technician")
    private List<ImagingSchedule> imagingSchedules;

    public Technician(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void addSchedule(ImagingSchedule schedule){
        imagingSchedules.add(schedule);
        schedule.setTechnician(this);
    }
    public void removeSchedule(ImagingSchedule schedule){
        imagingSchedules.remove(schedule);
        schedule.setTechnician(null);
    }
}
