package com.smartradiology.project.security;

import com.smartradiology.project.model.Admin;
import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.Technician;
import com.smartradiology.project.repository.AdminRepository;
import com.smartradiology.project.repository.DoctorRepository;
import com.smartradiology.project.repository.TechnicianRepository;
import com.smartradiology.project.security.jwt.AuthEntryPointJWT;
import com.smartradiology.project.security.jwt.AuthFilterToken;
import com.smartradiology.project.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJWT unAuthorizedHandler;

    @Bean
    public AuthFilterToken authenticationJwtTokenFilter(){
        return new AuthFilterToken();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET","POST","DELETE","PUT","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.
                cors(cors->{}).
                csrf(csrf->csrf.disable())
                .exceptionHandling(exception->exception.authenticationEntryPoint(unAuthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/api/admin/**").hasAuthority("ENTITY_ADMIN")
                            .requestMatchers("/api/doctor/**").hasAuthority("ENTITY_DOCTOR")
                            .requestMatchers("/api/technician/**").hasAuthority("ENTITY_TECHNICIAN")
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/api/public/**").permitAll()
                            .requestMatchers("/api/test/**").permitAll()
                            .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()

                        );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers(headers->headers.frameOptions(frameOptions->frameOptions.sameOrigin()));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        ));
    }

    @Bean
    public CommandLineRunner initData(DoctorRepository doctorRepository,
                                      AdminRepository adminRepository,
                                      TechnicianRepository technicianRepository,
                                      PasswordEncoder passwordEncoder){
        return args -> {
            if(!doctorRepository.existsByEmail("doc1@example.com")){
                Doctor doc = new Doctor("Anvar","doc1","Ortho","doc1@example.com",passwordEncoder.encode("docPass"));
                doctorRepository.save(doc);
            }
            if(!adminRepository.existsByEmail("admin@example.com")){
                Admin admin = new Admin("Albin","admin","admin@example.com",passwordEncoder.encode("adminPass"));
                adminRepository.save(admin);
            }
            if(!technicianRepository.existsByEmail("mriTech@example.com")){
                Technician technician = new Technician("Lakshmi","mriTech","mriTech@example.com", passwordEncoder.encode("techPass"));
                technicianRepository.save(technician);
            }
            if(!technicianRepository.existsByEmail("ctTech@example.com")){
                Technician technician = new Technician("Ashitha","ctTech","ctTech@example.com", passwordEncoder.encode("techPass"));
                technicianRepository.save(technician);
            }
            if(!technicianRepository.existsByEmail("xrayTech@example.com")){
                Technician technician = new Technician("Ananya","xrayTech","xrayTech@example.com", passwordEncoder.encode("techPass"));
                technicianRepository.save(technician);
            }
        };
    }




}
