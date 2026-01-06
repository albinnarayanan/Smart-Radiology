package com.smartradiology.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @JsonIgnore
    private String password;

    public Admin(String name,String userName, String email, String password) {
        this.name = name;
        this.username = userName;
        this.email = email;
        this.password = password;
    }
}