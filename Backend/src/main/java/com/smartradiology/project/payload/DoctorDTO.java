package com.smartradiology.project.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartradiology.project.model.RadiologyRequest;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonIgnore
    private List<RadiologyRequest> radiologyRequests;
}
