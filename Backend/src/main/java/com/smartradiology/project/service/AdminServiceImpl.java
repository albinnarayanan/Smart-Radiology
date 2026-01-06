package com.smartradiology.project.service;

import com.smartradiology.project.model.Doctor;
import com.smartradiology.project.model.Technician;
import com.smartradiology.project.payload.DoctorDTO;
import com.smartradiology.project.payload.DoctorListResponse;
import com.smartradiology.project.payload.TechnicianDTO;
import com.smartradiology.project.payload.TechnicianListResponse;
import com.smartradiology.project.repository.DoctorRepository;
import com.smartradiology.project.repository.TechnicianRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String DOCTORS_LIST_CACHE = "doctorsList";
    private static final String TECHNICIANS_LIST_CACHE = "techniciansList";



    @Override
    @Cacheable(value = DOCTORS_LIST_CACHE,
            key = "{#pageNumber, #pageSize, #sortBy, #sortOrder}")
    public DoctorListResponse getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Doctor> pageDoctors = doctorRepository.findAll(pageDetails);
        List<Doctor> doctors = pageDoctors.getContent();
        List<DoctorDTO> doctorDTOList = doctors.stream()
                .map(req->{
                    return modelMapper.map(req, DoctorDTO.class);
                }).toList();

        DoctorListResponse doctorListResponse = new DoctorListResponse();
        doctorListResponse.setContent(doctorDTOList);
        doctorListResponse.setPageNumber(pageDoctors.getNumber());
        doctorListResponse.setPageSize(pageDoctors.getSize());
        doctorListResponse.setTotalPages(pageDoctors.getTotalPages());
        doctorListResponse.setTotalElements(pageDoctors.getTotalElements());
        doctorListResponse.setLastPage(pageDoctors.isLast());


        return doctorListResponse;
    }

    @Override
    @Cacheable(value = TECHNICIANS_LIST_CACHE,
            key = "{#pageNumber, #pageSize, #sortBy, #sortOrder}")
    public TechnicianListResponse getAllTechnician(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
       Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                             ? Sort.by(sortBy).ascending()
                             : Sort.by(sortBy).descending();
       Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
       Page<Technician> technicianDTOPage = technicianRepository.findAll(pageDetails);
       List<Technician> technicians = technicianDTOPage.getContent();
       List<TechnicianDTO> technicianDTOS = technicians.stream()
               .map(tech->{
                   return modelMapper.map(tech, TechnicianDTO.class);
               }).toList();
       TechnicianListResponse technicianListResponse = new TechnicianListResponse();
       technicianListResponse.setContent(technicianDTOS);
       technicianListResponse.setPageNumber(technicianDTOPage.getNumber());
       technicianListResponse.setPageSize(technicianDTOPage.getSize());
       technicianListResponse.setTotalPages(technicianDTOPage.getTotalPages());
       technicianListResponse.setTotalElements(technicianDTOPage.getTotalElements());
       technicianListResponse.setLastPage(technicianDTOPage.isLast());


        return technicianListResponse;
    }

    @Override
    @CacheEvict(value = DOCTORS_LIST_CACHE, allEntries = true)
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctor.setPassword(passwordEncoder.encode(doctorDTO.getPassword()));
        Doctor savedDoctor = doctorRepository.save(doctor);

        return modelMapper.map(savedDoctor,DoctorDTO.class);
    }

    @Override
    @CacheEvict(value = TECHNICIANS_LIST_CACHE, allEntries = true)
    public TechnicianDTO createTechnician(TechnicianDTO technicianDTO) {
        Technician technician = modelMapper.map(technicianDTO, Technician.class);
        technician.setPassword(passwordEncoder.encode(technicianDTO.getPassword()));
        Technician savedTechnician = technicianRepository.save(technician);

        return modelMapper.map(savedTechnician,TechnicianDTO.class);

    }
}
