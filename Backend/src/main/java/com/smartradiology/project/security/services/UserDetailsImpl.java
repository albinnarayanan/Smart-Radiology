package com.smartradiology.project.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartradiology.project.model.Admin;
import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.Technician;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String name;
        private String username;
        private String email;
        @JsonIgnore
        private String password;
        private String entityType;
        private Collection<? extends GrantedAuthority> authorities;

        public UserDetailsImpl(Long id,String name, String username, String email, String password, String entityType) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.email = email;
            this.password = password;
            this.entityType = entityType;
            this.authorities = List.of(new SimpleGrantedAuthority("ENTITY_" + entityType.toUpperCase()));
        }

        // Factory methods for each entity
        public static UserDetailsImpl buildDoctor(Doctor doctor) {
            return new UserDetailsImpl(
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getUsername(),
                    doctor.getEmail(),
                    doctor.getPassword(),
                    "doctor"
            );
        }

        public static UserDetailsImpl buildTechnician(Technician technician) {
            return new UserDetailsImpl(
                    technician.getTechnicianId(),
                    technician.getName(),
                    technician.getUsername(),
                    technician.getEmail(),
                    technician.getPassword(),
                    "technician"
            );
        }

        public static UserDetailsImpl buildAdmin(Admin admin) {
            return new UserDetailsImpl(
                    admin.getAdminId(),
                    admin.getName(),
                    admin.getUsername() ,
                    admin.getEmail(),
                    admin.getPassword(),
                    "admin"
            );
        }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // standard UserDetails overrides...

}
