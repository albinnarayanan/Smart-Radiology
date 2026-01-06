package com.smartradiology.project.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartradiology.project.model.ImagingSchedule;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.List;

@Data
public class TechnicianDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private List<ImagingSchedule> imagingSchedules;
}
