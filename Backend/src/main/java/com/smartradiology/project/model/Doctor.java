package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;


    @Size(max = 30)
    private String name;

    @NotBlank
    private String username;

    @Size(max = 30)
    private String specialization;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "doctor",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<RadiologyRequest> radiologyRequests;

    public Doctor(String name,String username,String specialization, String email, String password) {
        this.name = name;
        this.specialization = specialization;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public void addRadiologyRequest(RadiologyRequest request) {
        radiologyRequests.add(request);
        request.setDoctor(this);
    }
}
