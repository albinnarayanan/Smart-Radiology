package com.smartradiology.project.payload;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class PatientDTO {
    private Long id;

    private String name;

    private String gender;

    private int age;

    private String contactNumber;
}
