package com.smartradiology.project.security.util;

import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.Technician;
import com.smartradiology.project.repository.DoctorRepository;
import com.smartradiology.project.repository.TechnicianRepository;
import com.smartradiology.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    public Doctor loggedInDoctor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Doctor doctor = doctorRepository.findByUsername(authentication.getName()).orElseThrow(
                ()->new UsernameNotFoundException("Doctor not found with username: "+authentication.getName())
        );
        return doctor;
    }
    public Technician loggedInTechnician(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Technician technician = technicianRepository.findByUsername(authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("Technician not found with username: "+authentication.getName()));

        return technician;
    }

    private UserDetailsImpl getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(!(principal instanceof UserDetailsImpl)){
            throw  new IllegalStateException("Unexpected principal type: "+principal.getClass());
        }
        return (UserDetailsImpl) principal;
    }

    public String loggedInEmail(){
        return getPrincipal().getEmail();
    }

    public Long loggedInUserId(){
        return getPrincipal().getId();
    }

    public String loggedInEntityType(){
        return getPrincipal().getEntityType();
    }

    public UserDetailsImpl loggedInUser(){
        return getPrincipal();
    }

    public boolean isDoctor() {
        return "doctor".equalsIgnoreCase(getPrincipal().getEntityType());
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(getPrincipal().getEntityType());
    }

    public boolean isTechnician() {
        return "technician".equalsIgnoreCase(getPrincipal().getEntityType());
    }



}
