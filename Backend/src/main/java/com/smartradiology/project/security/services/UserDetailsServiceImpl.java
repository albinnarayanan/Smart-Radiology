package com.smartradiology.project.security.services;

import com.smartradiology.project.repository.AdminRepository;
import com.smartradiology.project.repository.DoctorRepository;
import com.smartradiology.project.repository.TechnicianRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private TechnicianRepository technicianRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Default: try doctor first, then technician, then admin
        return doctorRepository.findByUsername(username)
                .map(UserDetailsImpl::buildDoctor)
                .or(() -> technicianRepository.findByUsername(username)
                        .map(UserDetailsImpl::buildTechnician))
                .or(() -> adminRepository.findByUsername(username)
                        .map(UserDetailsImpl::buildAdmin))
                .orElseThrow(() -> new UsernameNotFoundException("Entity not found with username: " + username));
    }

    // Alternatively: overload with entityType
    public UserDetails loadUserByUsernameAndEntityType(String username, String entityType) {
        switch (entityType.toLowerCase()) {
            case "doctor":
                return doctorRepository.findByUsername(username)
                        .map(UserDetailsImpl::buildDoctor)
                        .orElseThrow(() -> new UsernameNotFoundException("Doctor not found: " + username));
            case "technician":
                return technicianRepository.findByUsername(username)
                        .map(UserDetailsImpl::buildTechnician)
                        .orElseThrow(() -> new UsernameNotFoundException("Technician not found: " + username));
            case "admin":
                return adminRepository.findByUsername(username)
                        .map(UserDetailsImpl::buildAdmin)
                        .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));
            default:
                throw new UsernameNotFoundException("Unknown entity type: " + entityType);
        }
    }
}
